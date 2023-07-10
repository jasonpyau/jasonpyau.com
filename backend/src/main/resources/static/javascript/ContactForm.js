import { apiCall } from "./apiCall.js";

window.contactFormSubmit = contactFormSubmit;

async function contactFormSubmit() {
    const name = document.getElementById("NameInput").value;
    const contactInfo = document.getElementById("ContactInfoInput").value;
    const message = document.getElementById("MessageInput").value;
    const url = '/contact/send';
    const headers = {
        'Content-Type': 'application/json'
    };
    const body = {
        name: name,
        contactInfo: contactInfo,
        body: message
    };
    const result = await apiCall(url, "POST", headers, body);
    const json = await result.json();
    const contactFormSuccessElement = document.getElementById("ContactFormSuccess");
    const contactFormErrorElement = document.getElementById("ContactFormError");
    if (result.status === 200) {
        contactFormSuccessElement.style.display = "flex";
        contactFormErrorElement.style.display = "none";
    } else {
        contactFormSuccessElement.style.display = "none";
        contactFormErrorElement.style.display = "flex";
        contactFormErrorElement.textContent = json.reason;
        if (result.status !== 406) {
            alert(`Error in sending message, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
        }
    }
}