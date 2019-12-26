import Vue from "vue";
import axios from "axios";
import VueCookies from 'vue-cookies';
import router from "./router";

Vue.use(require('vue-cookies'));
Vue.use(VueCookies);

let jwtDecode = require('jwt-decode');

let subscribers = [];

const Auth = {

  logout() {
    this.deleteCookie(Auth.COOKIE_ACCESS_TOKEN);
    this.deleteCookie(Auth.COOKIE_REFRESH_TOKEN);
    router.push('login');
  },

  checkAuthenticate() {
    return $cookies.isKey(Auth.COOKIE_ACCESS_TOKEN);
  },

  refreshTokenInterceptor(Vue) {
    Vue.prototype.$axios.interceptors.response.use(
      function(response) {
        return response
      },
      function(error) {
        const errorResponse = error.response;

        if (Auth.isTokenExpiredError(errorResponse)) {
          return Auth.resetTokenAndReattemptRequest(error)
        }

        if (errorResponse.status === 401 || errorResponse.status === 403) {
          Auth.logout();
        }
        return Promise.reject(error)
      }
    )
  },

  setupVue(Vue, token) {
    Vue.prototype.$axios = axios.create({
      baseURL: process.env.VUE_APP_API_ENDPOINT,
      headers: {
        "Authorization": "Bearer " + token
      },
      transformResponse: axios.defaults.transformResponse.concat((data, headers) => {
        return data;
      })
    });
    Auth.refreshTokenInterceptor(Vue)
  },

  setAuthenTokenAPI(token) {
    Vue.use({
      install(Vue) {
        Auth.setupVue(Vue, token);
      }
    });
  },

  isTokenExpiredError(errorResponse) {
    return errorResponse.status === 401 && errorResponse.data.errorCode === 11;
  },

  saveRefreshToken(refreshToken) {
    $cookies.set(Auth.COOKIE_REFRESH_TOKEN, refreshToken, "6m", "/");
  },

  setCookie(token) {
    $cookies.set(Auth.COOKIE_ACCESS_TOKEN, token, "7d", "/");
  },

  deleteCookie(name) {
    $cookies.remove(name);
  },

  hasPermission(permissions) {
    if (!permissions || permissions.length === 0) {
      return true;
    }

    let token = $cookies.get(Auth.COOKIE_ACCESS_TOKEN);

    if (!token) {
      return false;
    }

    let jwtClaims = jwtDecode(token);

    if (!jwtClaims || !jwtClaims.scopes) {
      return false;
    }

    let userScopes = jwtClaims.scopes;
    for (let i = 0; i < permissions.length; i++) {
      if (userScopes.indexOf(permissions[i]) < 0) {
        return false;
      }
    }

    return true;
  },

  async resetTokenAndReattemptRequest(error) {
    try {
      const {response: errorResponse} = error;
      const refreshToken = $cookies.get(Auth.COOKIE_REFRESH_TOKEN);
      if (!refreshToken) {
        Auth.logout();
        return await Promise.reject(error);
      }
      const retryOriginalRequest = new Promise(resolve => {
        Auth.addSubscriber(access_token => {
          errorResponse.config.headers.Authorization = 'Bearer ' + access_token;
          resolve(axios(errorResponse.config));
        });
      });
      const response = await axios({
        method: 'post',
        url: process.env.VUE_APP_API_ENDPOINT + `/auth/token`,
        headers: {
          "Authorization": 'Bearer ' + refreshToken
        },
        data: {
          token: refreshToken
        }
      });
      if (!response.data) {
        Auth.logout();
        return await Promise.reject(error);
      }
      const newToken = response.data.token;
      Auth.setCookie(newToken);
      Auth.setAuthenTokenAPI(newToken);
      Auth.onAccessTokenFetched(newToken);
      return retryOriginalRequest;
    } catch (err) {
      Auth.logout();
      return await Promise.reject(err);
    }
  },

  onAccessTokenFetched(access_token) {
    subscribers.forEach(callback => callback(access_token));
    subscribers = [];
  },

  addSubscriber(callback) {
    subscribers.push(callback);
  },

  COOKIE_REFRESH_TOKEN: 'refresh-token',
  COOKIE_ACCESS_TOKEN: 'Token'

};
export default Auth;
