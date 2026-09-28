/**
 * app.js — shared utilities for OmniMarket
 * Load this on every page (it already is, via <script src="../js/app.js">).
 *
 * Token strategy:
 *   - The JWT access token is stored in localStorage (persists across page
 *     loads and browser restarts, until cleared).
 *   - The opaque refresh token lives in an httpOnly cookie set by the backend
 *     on login — JS never reads or writes it directly.
 *   - Alongside the token we store the "auth base" — the login endpoint with
 *     "/login" stripped off, e.g. "http://localhost:8080/api/customer-auth".
 *     /refresh and /logout are derived from it, so this works the same way
 *     for customer, seller, or admin logins without hardcoding any one URL.
 *   - App.authFetch(url, options) attaches the stored token to a request; if
 *     the backend responds 401 (token expired/invalid), it automatically
 *     calls "<authBase>/refresh" (sending the httpOnly cookie via
 *     credentials: 'include'), stores the new token, and retries the
 *     original request once.
 *
 * Also provides:
 *   - App.saveUser(user) / App.getUser() — the username the login endpoint
 *     returns alongside the token, for displaying who's signed in.
 *   - App.showToast(...) — used by the onclick="" handlers already in the templates.
 *   - Dashboard route guard: <body data-dashboard-role="..."> redirects to
 *     that role's login page if there's no token, fills in
 *     #current-user-display, and wires up #logout-btn to actually call
 *     "<authBase>/logout" before clearing local state.
 *
 * Login and register forms are each handled by their own dedicated script
 * (user-login.js, user-register.js, seller-register.js, ...) since every
 * backend endpoint has its own request/response shape.
 */

const CONFIG = {
  TOKEN_KEY: 'omnimarket_token',
  AUTH_BASE_KEY: 'omnimarket_auth_base',
  USER_KEY: 'omnimarket_user',
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
  // Token + user storage (localStorage)
  // ---------------------------------------------------------------------
  saveToken(token, authBase) {
    localStorage.setItem(CONFIG.TOKEN_KEY, token);
    if (authBase) localStorage.setItem(CONFIG.AUTH_BASE_KEY, authBase);
  },
  saveUser(user) {
    localStorage.setItem(CONFIG.USER_KEY, user);
  },
  getToken() {
    return localStorage.getItem(CONFIG.TOKEN_KEY);
  },
  getAuthBase() {
    return localStorage.getItem(CONFIG.AUTH_BASE_KEY);
  },
  getUser() {
    return localStorage.getItem(CONFIG.USER_KEY);
  },
  clearToken() {
    localStorage.removeItem(CONFIG.TOKEN_KEY);
    localStorage.removeItem(CONFIG.AUTH_BASE_KEY);
    localStorage.removeItem(CONFIG.USER_KEY);
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
      const authBase = App.getAuthBase();
      if (!authBase) {
        App.clearToken();
        return res; // no known auth base — caller should treat this as logged out
      }

      const refreshRes = await fetch(`${authBase}/refresh`, {
        method: 'POST',
        credentials: 'include', // sends the httpOnly refresh-token cookie
      });

      if (!refreshRes.ok) {
        App.clearToken();
        return res; // refresh failed too — session is really over
      }

      const newToken = await refreshRes.text(); // the auth controllers return plain text
      App.saveToken(newToken, authBase);
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

  // ---------------------------------------------------------------------
  // Logout: revoke server-side, then clear local state either way
  // ---------------------------------------------------------------------
  async logout(redirectTo) {
    const token = App.getToken();
    const authBase = App.getAuthBase();

    if (token && authBase) {
      try {
        // customerAuthController's logout takes accessToken as a plain,
        // unannotated String parameter, which Spring MVC resolves as a
        // request/query parameter by default — the refreshToken cookie
        // rides along automatically via credentials: 'include'.
        await fetch(`${authBase}/logout?accessToken=${encodeURIComponent(token)}`, {
          method: 'POST',
          credentials: 'include',
        });
      } catch (err) {
        // Network hiccup — still log the person out locally below.
      }
    }

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

  const nameEl = document.getElementById('current-user-display');
  if (nameEl && App.getUser()) nameEl.textContent = App.getUser();

  const logoutBtn = document.getElementById('logout-btn');
  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => App.logout(`${role}-login.html`));
  }
}

document.addEventListener('DOMContentLoaded', () => {
  wireDashboard();
});



