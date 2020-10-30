import {JSON_TYPE,URL_ENCODED_TYPE,HEADER_BEARER,HEADER_BASIC} from "../Constants";
import {getPublicHeader} from "../utils/util";


export const api = async (url, method, body = null, headers = {}) => {
  try {
    const reqBody = body;
    const fetchParams = { method, headers };

    if ((method === "POST" || method === "PUT") && !reqBody) {
      throw new Error("Request body required");
    }

    if (reqBody) {
     // fetchParams.headers["Content-type"] = "x-www-form-urlencoded";
      //fetchParams.headers["Accept"] = "application/json";
      fetchParams.body = reqBody;
    }
  
    console.log('INVOCACION A SERVICIO ',url);
   
    let fetchPromise = {};
    if (method == "POST") {
      fetchPromise = fetch(url, {
        method: method,
        headers: fetchParams.headers,
        body: fetchParams.body
      });
    }
    if (method == "PUT") {
      fetchPromise = fetch(url, {
        method: method,
        headers: fetchParams.headers,
        body: fetchParams.body
      });
    }

    if (method == "GET") {
      fetchPromise = fetch(url, {
        method: method,
        headers: headers
      });
    }

    const timeOutPromise = new Promise((resolve, reject) => {
      setTimeout(() => {
        reject("Request Timeout");
      }, 55000);
    });

    const response = await Promise.race([fetchPromise,timeOutPromise]);

    return response;
  } catch (e) {
    return e;
  }
};




export const fetchApi = async (
  url,
  method,
  body,
  statusCode,
  token = null,
  type ,
  authenticationType = null,
) => {
  try {
    const headers = {};
    const result = {
     // token: null,
      success: false,
      responseBody: null
    };

    
    let authType=(authenticationType == null?HEADER_BEARER:HEADER_BASIC)

    
    
    if (token) {
      headers["Authorization"] = `${authType} ${token}`;
  }
    if(type == JSON_TYPE){
      headers["Content-Type"] ="application/json",
      headers["Accept"] ="application/json"
    }
    if(type == URL_ENCODED_TYPE){
      headers["Content-Type"] ="x-www-form-url-encoded"
    }
  
 
    const response = await api(url, method, body, headers);

    if (response.status === statusCode) {
      result.success = true;

      let responseBody;
      const responseText = await response.text();

      try {
        responseBody = JSON.parse(responseText);
      } catch (e) {
        responseBody = responseText;
      }

      result.responseBody = responseBody;
      return result;
    }

    let errorBody;
    const errorText = await response.text();

    try {
      errorBody = JSON.parse(errorText);
    } catch (e) {
      errorBody = errorText;
    }

    result.responseBody = errorBody;

    
    throw result;
  } catch (error) {
    return error;
  }
};

export function ReactfetchApi(url, token) {
  return fetch(url, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
}

export function reactfetchApiGet(url, token) {
  return fetch(url, {
    headers: {
      Authorization: `Bearer ${token}`
    }
  });
}


export const reactfetchApiPost = async (url, token, data) => {
  return fetch(url, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
      Accept: "application/json"
    },
    body: data
  });
};
