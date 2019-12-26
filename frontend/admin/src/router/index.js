import Vue from "vue";
import VueRouter from "vue-router";
import routes from "./routes";
import general from "./general";
import Auth from "../auth";

Vue.use(VueRouter);

// configure router
const router = new VueRouter({
  mode: "history",
  routes: [...routes, ...general], // short for routes: routes
  linkActiveClass: "active"
});

router.beforeEach((to, from, next) => {
  const {permissions} = to.meta;

  if (!Auth.checkAuthenticate()) {
    Auth.deleteCookie(Auth.COOKIE_ACCESS_TOKEN);
    if (to.path !== '/login' || to.name !== 'login') {
      return next({ path: '/login', query: { returnUrl: to.path } });
    }
  }

  if (!permissions || permissions.length === 0) {
    next();
  }

  if (!Auth.hasPermission(permissions)) {
    return next({path: '/'});
  }

  next();
});

export default router;
