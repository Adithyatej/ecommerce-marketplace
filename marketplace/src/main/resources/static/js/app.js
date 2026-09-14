document.querySelectorAll("form[data-endpoint]").forEach((form) => {
form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const message = form.querySelector(".message");
    const button = form.querySelector("button[type='submit']");
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    button.disabled = true;
    message.className = "message";
    message.textContent = "Submitting...";

    try {
      const response = await fetch(form.dataset.endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
      });
      // Your @RestControllerAdvice returns a plain String for errors.
      // Read the response as text first so both text and JSON responses work.
      const responseText = await response.text();
      let result = {};

      try {
        result = responseText ? JSON.parse(responseText) : {};
      } catch {
        result = { message: responseText };
      }

      if (!response.ok) {
        throw new Error(result.message || "Registration failed. Please try again.");
      }

      message.className = "message success";
      message.textContent = result.message || "Seller account created successfully.";
      form.reset();
    } catch (error) {
      message.className = "message error";
      message.textContent = error.message;
    } finally {
      button.disabled = false;
    }
  });
});
