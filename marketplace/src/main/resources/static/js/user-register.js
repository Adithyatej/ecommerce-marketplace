/**
 * user-register.js
 * Handles the "Create Customer Account" form on user-register.html.
 *
 * Flow:
 *   1. e.preventDefault() — stop the browser's default full-page form submit.
 *   2. Read the form's fields into a plain object, send it to the backend as JSON.
 *   3. Success -> show a message, then redirect to login.html.
 *   4. Failure -> show the backend's error message in the .message div.
 */
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('user-register-form');
  if (!form) return;

  const messageEl = form.querySelector('.message');
  const submitBtn = form.querySelector('button[type="submit"]');
  const originalLabel = submitBtn.textContent;

  form.addEventListener('submit', async (event) => {
    event.preventDefault(); // must-have: stops the normal navigation/page reload

    // FormData reads every named input in the form ({ name, email, password,
    // phoneNumber }); Object.fromEntries turns that into a plain JS object
    // so JSON.stringify can send it as a JSON body.
    const formData = new FormData(form);
    const payload = Object.fromEntries(formData.entries());

    submitBtn.disabled = true;
    submitBtn.textContent = 'Creating account…';

    try {
      const response = await fetch(form.dataset.endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        showMessage('success', 'Account created successfully! Redirecting to login…');
        setTimeout(() => {
          window.location.href = 'login.html';
        }, 1200);
        return; // leave the button disabled — the page is about to navigate away
      }

      // Non-2xx response: Spring's error body is usually { "message": "..." }
      const errorBody = await response.json().catch(() => ({}));
      showMessage('error', errorBody.message || `Registration failed (${response.status}).`);
      resetButton();
    } catch (err) {
      // fetch() itself threw — server down, wrong port, CORS, etc.
      showMessage('error', 'Could not reach the server. Is the backend running on localhost:8080?');
      resetButton();
    }
  });

  function showMessage(type, text) {
    // type is 'success' or 'error' — matches the .message.success / .message.error CSS
    messageEl.textContent = text;
    messageEl.className = `message visible ${type}`;
  }

  function resetButton() {
    submitBtn.disabled = false;
    submitBtn.textContent = originalLabel;
  }
});