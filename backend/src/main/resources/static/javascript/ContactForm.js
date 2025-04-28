import { apiCall } from "./apiCall.js";

window.contactFormSubmit = contactFormSubmit;

async function contactFormSubmit(event) {
    event.preventDefault();
    const contactFormSuccessElement = document.getElementById("ContactFormSuccess");
    const contactFormErrorElement = document.getElementById("ContactFormError");
    contactFormSuccessElement.style.display = "none";
    contactFormErrorElement.style.display = "none";
    const form = event.srcElement.form;
    if (!form) {
        return;
    }
    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }
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
    document.getElementById("ContactMeSendButton").classList.add("disabled");
    document.getElementById("ContactMeSpinner").style.display = "block";
    const result = await apiCall(url, "POST", headers, body);
    const json = await result.json();
    document.getElementById("ContactMeSendButton").classList.remove("disabled");
    document.getElementById("ContactMeSpinner").style.display = "none";
    if (result.status === 200) {
        contactFormSuccessElement.style.display = "flex";
    } else {
        contactFormErrorElement.style.display = "flex";
        contactFormErrorElement.textContent = json.reason;
        if (result.status !== 406) {
            alert(`Error in sending message, refresh. If this problem persists, contact me.\n\nReason: ${json.reason}`);
        }
    }
}