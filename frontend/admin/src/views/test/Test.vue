<template>
  <div>
    <h1>Home</h1>
    <h2>Connected? {{ connected}}</h2>
    <div>
      <button @click="tickleConnection">{{ connected ? 'disconnect' : 'connect' }}</button>
    </div>
    <h2>Send Message</h2>
    <div>
      <input v-model="send_message" placeholder="Send Message">
      <button @click="send">Send</button>
    </div>
    <h2>Message Received</h2>
    <p>{{ received_messages }}</p>
    <v-dialog v-model="createDialog" max-width="800">
      <v-card>
        <v-card-title class="pa-3">
          <v-layout row class="pa-0">
            <v-flex xs6>{{ received_messages }}</v-flex>
            <v-spacer></v-spacer>
          </v-layout>
        </v-card-title>
        <v-divider class="ma-0"></v-divider>
        <v-card-actions>
          <v-btn color="red darken-1 white--text" small @click="createDialog = false">Đóng</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
    import SockJS from 'sockjs-client'
    import Stomp from 'webstomp-client'
    export default {
        name: 'test',
        data () {
            return {
                received_messages: "",
                send_message: null,
                connected: false,
                createDialog: false
            }
        },
        methods: {
            send () {
                console.log('Send message:' + this.send_message);
                if (this.stompClient && this.stompClient.connected) {
                    this.stompClient.send('/app/hello', this.send_message)
                }
            },
            connect () {
                this.socket = new SockJS(process.env.VUE_APP_API_ENDPOINT+'/socket');
                this.stompClient = Stomp.over(this.socket);
                this.stompClient.connect({}, (frame) => {
                    this.connected = true
                    // this.stompClient.subscribe('/topic/greetings', (tick) => {
                    //     console.log(tick.body);
                    //     this.received_messages = tick.body;
                    //     // this.createDialog = true;
                    //     setTimeout(this.closeDialog, 3000)
                    // })
                }, (error) => {
                    this.connected = false
                })
            },
            disconnect () {
                if (this.stompClient) {
                    this.stompClient.disconnect()
                }
                this.connected = false
            },
            tickleConnection () {
                this.connected ? this.disconnect() : this.connect()
            },
            closeDialog(){
                this.createDialog = false;
            },
            handler: function handler(event) {
                this.disconnect();
            }
        },
        mounted () {
            this.connect()
        },
        created() {
            document.addEventListener('beforeunload', this.handler)
        }
    }
</script>
