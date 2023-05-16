import { SERVER_URL } from "./constants.js";
import { apiCall } from "./apiCall.js";

window.contactFormSubmit = contactFormSubmit;

async function contactFormSubmit() {
    const name = document.getElementById("NameInput").value;
    const contactInfo = document.getElementById("ContactInfoInput").value;
    const message = document.getElementById("MessageInput").value;
    const url = `${SERVER_URL}/contact/send`;
    const headers = {};
    const body = {
        name: name,
        contactInfo: contactInfo,
        body: message
    };
    console.log(body)
    const result = await apiCall(url, "PUT", headers, body);
    const json = await result.json();
    const contactFormSuccessElement = document.getElementById("ContactFormSuccess");
    const contactFormErrorElement = document.getElementById("ContactFormError");
    if (result.status === 200) {
        contactFormSuccessElement.style.display = "flex";
        contactFormErrorElement.style.display = "none";
    } else {
        contactFormSuccessElement.style.display = "none";
        contactFormErrorElement.style.display = "flex";
        contactFormErrorElement.textContent = json.status;
    }
}