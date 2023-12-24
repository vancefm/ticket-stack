export function checkForResponseErrors(requestMethod, requestBodyJson, fetchResponse) {
    if(fetchResponse.status !== 200) {
        console.log("Method: %s\nBody: %s\nResponse Status: %s %s\nErrors: %o", requestMethod, requestBodyJson, fetchResponse.status, fetchResponse.statusText, fetchResponse["errors"]);
    }
}
