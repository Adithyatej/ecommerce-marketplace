/**
 * seller-register.js
 * Handles the "Open Your Seller Storefront" form on seller-register.html.
 *
 * Same flow as user-register.js, with one difference: sellerRequestDTO on
 * the backend nests the address fields under a "sellerAddress" object, so
 * we can't just do Object.fromEntries(formData) like the flat user form —
 * the payload has to be built by hand to match that shape:
 *
 *   {
 *     name, email, password, phoneNumber, storeName, storeDescription,
 *     sellerAddress: { doorNumber, street, city, state, pinCode }
 *   }
 */
document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('seller-register-form');
  if (!form) return;

  const messageEl = form.querySelector('.message');
  const submitBtn = form.querySelector('button[type="submit"]');
  const originalLabel = submitBtn.textContent;

  form.addEventListener('submit', async (event) => {
    event.preventDefault(); // must-have: stops the normal navigation/page reload

    const formData = new FormData(form);

    // Built explicitly (rather than Object.fromEntries) so the address
    // fields land inside their own "sellerAddress" object, matching
    // sellerRequestDTO / sellerAddressDTO on the backend.
    const payload = {
      name: formData.get('name'),
      email: formData.get('email'),
      password: formData.get('password'),
      phoneNumber: formData.get('phoneNumber'),
      storeName: formData.get('storeName'),
      storeDescription: formData.get('storeDescription'),
      sellerAddress: {
        doorNumber: formData.get('doorNumber'),
        street: formData.get('street'),
        city: formData.get('city'),
        state: formData.get('state'),
        pinCode: formData.get('pinCode'),
      },
    };

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
    messageEl.textContent = text;
    messageEl.className = `message visible ${type}`;
  }

  function resetButton() {
    submitBtn.disabled = false;
    submitBtn.textContent = originalLabel;
  }
});