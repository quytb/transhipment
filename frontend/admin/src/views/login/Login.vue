<template>
  <div id="app">
    <v-app id="inspire">
      <v-content>
        <v-container fluid fill-height>
          <v-layout align-center justify-center>
            <v-flex xs12 sm8 md4>
              <v-card class="elevation-12">
                <v-toolbar dark color="primary">
                  <v-toolbar-title>Trung Chuyển</v-toolbar-title>
                  <v-spacer></v-spacer>
                </v-toolbar>
                <v-card-text>
                  <v-form>
                    <v-text-field prepend-icon="person" name="login" label="Tên đăng nhập" v-model="loginData.username" type="text" autofocus placeholder=" "></v-text-field>
                    <v-text-field prepend-icon="lock" name="password" @keyup.enter.native="login()" label="Mật khẩu" v-model="loginData.password" id="password" type="password" placeholder=" "></v-text-field>
                  </v-form>
                </v-card-text>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="primary" @click="login()">Login</v-btn>
                </v-card-actions>
              </v-card>
            </v-flex>
            <!-- Snackbar -->
            <v-snackbar
              v-model="snackbar.active"
              :color="snackbar.color"
              :multi-line="false"
              :timeout="5000"
              top
              right
              style="z-index: 1000;"
            >
              <span class="white--text">{{ snackbar.text }}</span>
              <v-btn
                dark
                flat
                @click="snackbar.active = false"
              >
                Đóng
              </v-btn>
            </v-snackbar>
          </v-layout>
        </v-container>
      </v-content>
    </v-app>
  </div>
</template>

<script>
    import Auth from "../../auth";

    export default {
        name: "Login",
        data() {
          return {
            loginData : {},
            // Message snackbar
            snackbar : {
              active  : false,
              text    : '',
              color   : 'red'
            },
            userData : []
          }
        },
        methods : {
          async login(){
            try {
              let res = await this.$axios.post(`/login`, this.loginData);
              this.userData = res.data;
              Auth.setCookie(this.userData.accessToken);
              Auth.saveRefreshToken(this.userData.refreshToken);
              Auth.setAuthenTokenAPI(this.userData.accessToken);
              this.$router.push("/");
            } catch(err) {
              this.snackbar.active  = true;
              this.snackbar.color   = "red";
              this.snackbar.text    = err.response.data.message;
            }
          }
        }
    }


</script>

<style scoped>

</style>
