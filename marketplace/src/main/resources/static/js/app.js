/**
 * app.js — shared utilities for OmniMarket
 * Load this on every page (it already is, via <script src="../js/app.js">).
 *
 * Token strategy:
 *   - The JWT access token is stored in localStorage (persists across page
 *     loads and browser restarts, until cleared).
 *   - The opaque refresh token lives in an httpOnly cookie set by the backend
 *     on login — JS never reads or writes it directly.
 *   - App.authFetch(url, options) attaches the stored token to a request; if
 *     the backend responds 401 (token expired/invalid), it automatically
 *     calls the matching refresh endpoint (sending the httpOnly cookie via
 *     credentials: 'include'), stores the new token, and retries the
 *     original request once.
 *   - The refresh endpoint is derived from whichever login endpoint the
 *     token came from (".../login" -> ".../refresh") and stored alongside
 *     the token, so this works the same way for customer, seller, or admin
 *     logins without hardcoding one URL here.
 *
 * Also provides:
 *   - App.showToast(...) — used by the onclick="" handlers already in the templates.
 *   - Dashboard route guard: <body data-dashboard-role="..."> redirects to
 *     that role's login page if there's no token, and wires up #logout-btn.
 *
 * Login and register forms are each handled by their own dedicated script
 * (user-login.js, user-register.js, seller-register.js, ...) since every
 * backend endpoint has its own request/response shape.
 */

const CONFIG = {
  TOKEN_KEY: 'omnimarket_token',
  REFRESH_ENDPOINT_KEY: 'omnimarket_refresh_endpoint',
};

const App = {
  // ---------------------------------------------------------------------
  // Toast notifications (used by onclick="App.showToast('...')" in the HTML)
  // ---------------------------------------------------------------------
  showToast(message, duration = 3000) {
    let toast = document.querySelector('.toast-notice');
    if (!toast) {
      toast = document.createElement('div');
      toast.className = 'toast-notice';
      document.body.appendChild(toast);
    }
    toast.textContent = message;
    toast.style.display = 'block';
    clearTimeout(toast._hideTimer);
    toast._hideTimer = setTimeout(() => {
      toast.style.display = 'none';
    }, duration);
  },

  // ---------------------------------------------------------------------
  // Inline <div class="message"> feedback inside a form
  // ---------------------------------------------------------------------
  setMessage(formEl, text, type) {
    const el = formEl.querySelector('.message');
    if (!el) return;
    el.textContent = text;
    el.className = `message visible ${type}`;
  },

  // ---------------------------------------------------------------------
  // Token storage (localStorage)
  // ---------------------------------------------------------------------
  saveToken(token, refreshEndpoint) {
    localStorage.setItem(CONFIG.TOKEN_KEY, token);
    if (refreshEndpoint) localStorage.setItem(CONFIG.REFRESH_ENDPOINT_KEY, refreshEndpoint);
  },
  getToken() {
    return localStorage.getItem(CONFIG.TOKEN_KEY);
  },
  getRefreshEndpoint() {
    return localStorage.getItem(CONFIG.REFRESH_ENDPOINT_KEY);
  },
  clearToken() {
    localStorage.removeItem(CONFIG.TOKEN_KEY);
    localStorage.removeItem(CONFIG.REFRESH_ENDPOINT_KEY);
  },

  // ---------------------------------------------------------------------
  // Authenticated fetch with automatic refresh-and-retry on a 401
  // ---------------------------------------------------------------------
  async authFetch(url, options = {}) {
    const withAuthHeader = (token) => ({
      ...options,
      headers: { ...(options.headers || {}), Authorization: `Bearer ${token}` },
    });

    let res = await fetch(url, withAuthHeader(App.getToken()));

    if (res.status === 401) {
      const refreshEndpoint = App.getRefreshEndpoint();
      if (!refreshEndpoint) {
        App.clearToken();
        return res; // no known refresh endpoint — caller should treat this as logged out
      }

      const refreshRes = await fetch(refreshEndpoint, {
        method: 'POST',
        credentials: 'include', // sends the httpOnly refresh-token cookie
      });

      if (!refreshRes.ok) {
        App.clearToken();
        return res; // refresh failed too — session is really over
      }

      const newToken = await refreshRes.text(); // the auth controllers return plain text
      App.saveToken(newToken, refreshEndpoint);
      res = await fetch(url, withAuthHeader(newToken)); // retry once with the fresh token
    }

    return res;
  },

  // ---------------------------------------------------------------------
  // Dashboard route guard — role is lowercase: 'user' | 'seller' | 'admin'
  // ---------------------------------------------------------------------
  requireAuth(expectedRole) {
    if (!App.getToken()) {
      window.location.href = `${expectedRole}-login.html`;
      return false;
    }
    return true;
  },

  logout(redirectTo) {
    App.clearToken();
    window.location.href = redirectTo;
  },
};

// ---------------------------------------------------------------------------
// Dashboard pages: <body data-dashboard-role="user|seller|admin">
// ---------------------------------------------------------------------------
function wireDashboard() {
  const role = document.body.dataset.dashboardRole;
  if (!role) return; // not a dashboard page

  if (!App.requireAuth(role)) return; // redirects to the right login page if not signed in

  const logoutBtn = document.getElementById('logout-btn');
  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => App.logout(`${role}-login.html`));
  }
  // Populating #current-user-display with the person's name isn't wired up
  // yet — we'll add that when we build the dashboard pages themselves.
}

document.addEventListener('DOMContentLoaded', () => {
  wireDashboard();
});