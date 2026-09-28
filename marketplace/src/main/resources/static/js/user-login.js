/**
 * user-login.js
 * Handles the "Sign In as Customer" form on user-login.html.
 *
 * Read straight from customerAuthController:
 *   - POST /api/customer-auth/login expects a JSON body: { "username": "...", "password": "..." }
 *     (the email the person types is sent under the "username" key).
 *   - On success, the body is JSON: { "user": "...", "Access_Token": "..." }.
 *   - On failure, the body is plain text, e.g. "username or password is incorrect" — a
 *     different shape than success, so each branch reads the response differently.
 *   - On success, the response also sets an httpOnly cookie holding the opaque
 *     refresh token — the browser stores/sends it automatically; we never
 *     touch it directly. fetch just needs credentials: 'include' for that to work.
 *
 * The access token and username are saved via App.saveToken(...) / App.saveUser(...)
 * from app.js, along with the auth base URL (".../customer-auth"), which is what
 * lets user-home.html (or any other page) find them after this page redirects,
 * and lets App.authFetch / App.logout derive the /refresh and /logout endpoints
 * later without this script needing to know about them.
 */
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('user-login-form');
  if (!form) return;

  const messageEl = form.querySelector('.message');
  const submitBtn = form.querySelector('button[type="submit"]');
  const originalLabel = submitBtn.textContent;

  form.addEventListener('submit', async (event) => {
    event.preventDefault(); // must-have: stops the normal navigation/page reload

    const formData = new FormData(form);
    const payload = {
      username: formData.get('email'), // LoginRequestDTO field is "username"
      password: formData.get('password'),
    };

    submitBtn.disabled = true;
    submitBtn.textContent = 'Signing in…';

    try {
      const response = await fetch(form.dataset.endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include', // required for the browser to store the httpOnly refresh cookie
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        // Success body is now JSON: { "user": "...", "Access_Token": "..." }
        const data = await response.json();

        // ".../customer-auth/login" -> ".../customer-auth" (used to derive
        // both /refresh and /logout later, from app.js)
        const authBase = form.dataset.endpoint.replace(/\/login$/, '');
        App.saveToken(data.Access_Token, authBase);
        App.saveUser(data.user);

        showMessage('success', 'Login successful — redirecting…');
        setTimeout(() => {
          window.location.href = form.dataset.redirect; // user-home.html
        }, 400);
        return; // leave the button disabled — the page is about to navigate away
      }

      // Failure body is still plain text, e.g. "username or password is incorrect"
      const bodyText = await response.text();
      showMessage('error', extractMessage(bodyText) || `Login failed (${response.status}).`);
      resetButton();
    } catch (err) {
      showMessage('error', 'Could not reach the server. Is the backend running on localhost:8080?');
      resetButton();
    }
  });

  function extractMessage(raw) {
    try {
      const parsed = JSON.parse(raw);
      return parsed.message || parsed.error || raw;
    } catch {
      return raw;
    }
  }

  function showMessage(type, text) {
    messageEl.textContent = text;
    messageEl.className = `message visible ${type}`;
  }

  function resetButton() {
    submitBtn.disabled = false;
    submitBtn.textContent = originalLabel;
  }
});