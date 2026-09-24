

document.addEventListener("DOMContentLoaded", () => {
    const registerForm = document.getElementById("user-register-form");
    if (!registerForm) return;

    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const messageEl = registerForm.querySelector(".message");
        const submitBtn = registerForm.querySelector("button[type='submit']");

        // 1. Read Form-Data: name, email, password, phoneNumber
        const formData = new FormData(registerForm);
        const registrationData = {
            name: (formData.get("name") || "").trim(),
            email: (formData.get("email") || "").trim(),
            password: formData.get("password") || "",
            phoneNumber: (formData.get("phoneNumber") || "").trim()
        };


        let targetEndpoint = registerForm.dataset.endpoint || "/api/customer-auth/register";
        if (!targetEndpoint.startsWith("http")) {
            const isSpringPort = window.location.port === "8080";
            if (!isSpringPort) {
                targetEndpoint = `http://localhost:8080${targetEndpoint}`;
            }
        }

        const loginRedirect = registerForm.dataset.loginRedirect || "user-login.html";

        // UI Loading State
        submitBtn.disabled = true;
        const originalBtnText = submitBtn.textContent;
        submitBtn.textContent = "Registering...";
        
        if (messageEl) {
            messageEl.className = "message visible";
            messageEl.style.color = "#4f46e5";
            messageEl.style.background = "#eef2ff";
            messageEl.style.border = "1px solid #c7d2fe";
            messageEl.textContent = "Submitting registration to server...";
        }

        try {
            // 3. Trigger HTTP POST API call with JSON body
            const response = await fetch(targetEndpoint, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json, text/plain, */*"
                },
                body: JSON.stringify(registrationData) // JSONified payload
            });

            // 4. Read response as text first (handles both plain string & JSON response formats)
            const responseText = await response.text();

            // 5. Check if registration FAILED
            if (!response.ok) {
                let failureReason = "";

                try {
                    const errorJson = JSON.parse(responseText);

                    // Case A: Spring Boot MethodArgumentNotValidException with field validation errors
                    if (errorJson.errors && Array.isArray(errorJson.errors) && errorJson.errors.length > 0) {
                        failureReason = errorJson.errors
                            .map(err => `• ${err.field ? `<strong>${err.field}</strong>: ` : ""}${err.defaultMessage || err.message}`)
                            .join("<br/>");
                    }
                    // Case B: General JSON error message
                    else if (errorJson.message) {
                        failureReason = errorJson.message;
                    } else if (errorJson.error) {
                        failureReason = errorJson.error;
                    }
                } catch {
                    // Case C: Plain text error returned by @RestControllerAdvice (e.g. globalExceptionHandler body)
                    failureReason = responseText || `HTTP ${response.status} ${response.statusText}`;
                }

                if (!failureReason || failureReason.trim().length === 0) {
                    failureReason = `Server returned status ${response.status} (${response.statusText || "Bad Request"})`;
                }

                // Show WHY registration failed
                if (messageEl) {
                    messageEl.className = "message visible error";
                    messageEl.style.color = "";
                    messageEl.style.background = "";
                    messageEl.style.border = "";
                    messageEl.innerHTML = `
                        <div style="line-height: 1.5;">
                            <strong>Registration Failed:</strong><br/>
                            ${failureReason}
                        </div>
                    `;
                }
                return;
            }

            // 6. Registration SUCCESSFUL
            // Format success message from server or fallback
            let successMessage = "User registered successfully";
            if (responseText && responseText.trim().length > 0) {
                try {
                    const successJson = JSON.parse(responseText);
                    successMessage = successJson.message || successJson.name || successMessage;
                } catch {
                    // Server returned plain text e.g. "John Doe registered successfully"
                    successMessage = responseText;
                }
            }

            // Display success message and proceed to login
            if (messageEl) {
                messageEl.className = "message visible success";
                messageEl.style.color = "";
                messageEl.style.background = "";
                messageEl.style.border = "";
                messageEl.innerHTML = `
                    <div style="display: flex; flex-direction: column; gap: 8px;">
                        <span>✓ <strong>${successMessage}</strong></span>
                        <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 10px; margin-top: 4px;">
                            <span style="font-size: 0.85rem; color: #065f46;">Proceeding to login in <span id="countdown">2</span>s...</span>
                            <a href="${loginRedirect}" class="btn btn-sm btn-primary" style="text-decoration: none;">
                                Proceed to Login Now →
                            </a>
                        </div>
                    </div>
                `;
            }

            registerForm.reset();

            // Countdown and auto-redirect to login
            let secondsLeft = 2;
            const countdownEl = document.getElementById("countdown");
            const countdownTimer = setInterval(() => {
                secondsLeft--;
                if (countdownEl) countdownEl.textContent = secondsLeft;
                if (secondsLeft <= 0) {
                    clearInterval(countdownTimer);
                    window.location.href = loginRedirect;
                }
            }, 1000);

        } catch (networkError) {
            // Handle Network / Connection errors (e.g. backend server is not running)
            console.error("Network error during customer registration:", networkError);
            if (messageEl) {
                messageEl.className = "message visible error";
                messageEl.style.color = "";
                messageEl.style.background = "";
                messageEl.style.border = "";
                messageEl.innerHTML = `
                    <strong>Connection Failed:</strong> Unable to reach auth server at <code>${targetEndpoint}</code>.<br/>
                    <span style="font-size: 0.825rem; color: #b91c1c;">Please verify your Spring Boot application is running and accessible.</span>
                `;
            }
        } finally {
            submitBtn.disabled = false;
            submitBtn.textContent = originalBtnText;
        }
    });
});

