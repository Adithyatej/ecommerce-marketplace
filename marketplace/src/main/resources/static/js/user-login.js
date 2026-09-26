/**
* user-login.js
* Handles the "Sign In as Customer" form on user-login.html.
*
* Read straight from customerAuthController:
*   - POST /api/customer-auth/login expects a JSON body: { "username": "...", "password": "..." }
*     (the email the person types is sent under the "username" key).
*   - Both success and failure bodies are plain text, not JSON:
*       success -> the raw JWT access token string
*       failure -> a message like "username or password is incorrect"
*   - On success, the response also sets an httpOnly cookie holding the opaque
*     refresh token — the browser stores/sends it automatically; we never
*     touch it directly. fetch just needs credentials: 'include' for that to work.
*
* The access token itself is saved via App.saveToken(...) from app.js, which
* puts it in localStorage alongside the matching refresh endpoint. That's what
* lets user-home.html (or any other page) find the token after this page
* redirects, and lets App.authFetch auto-refresh it later once it expires.
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

const bodyText = await response.text(); // plain text either way, not JSON

if (response.ok) {
// ".../customer-auth/login" -> ".../customer-auth/refresh"
const refreshEndpoint = form.dataset.endpoint.replace(/\/login$/, '/refresh');
console.log(App.getToken())
App.saveToken(bodyText, refreshEndpoint);

showMessage('success', 'Login successful — redirecting…');
setTimeout(() => {
window.location.href = form.dataset.redirect; // user-home.html
}, 400);
return; // leave the button disabled — the page is about to navigate away
}

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