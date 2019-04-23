
window.app = new Vue({
    el: "#app",
    data:
        {
            searchQuery: '',
            gridColumns: [],
            gridData: [],

            state: unlogined,
            currentWindow: 'signIn',
            token: '',
        },
    mounted() {
      if (localStorage.searchQuery) {
        this.searchQuery = localStorage.searchQuery;
      }
      if (localStorage.gridColumns) {
        this.gridColumns = localStorage.gridColumns;
      }
      if (localStorage.gridData) {
        this.gridData = localStorage.gridData;
      }
      if (localStorage.state) {
        this.state = localStorage.state;
      }
      if (localStorage.currentWindow) {
        this.currentWindow = localStorage.currentWindow;
      }
      if (localStorage.token) {
        this.token = localStorage.token;
      }
    },
    watch: {
      searchQuery(newsearchQuery) {
        localStorage.searchQuery = newsearchQuery;
      },
      gridColumns(newgridColumns) {
        localStorage.gridColumns = newgridColumns;
      },
      gridData(newgridData) {
        localStorage.gridData = newgridData;
      },
      state(newstate) {
        localStorage.state = newstate;
      },
      currentWindow(newcurrentWindow) {
        localStorage.currentWindow = newcurrentWindow;
      },
      token(newtoken) {
        localStorage.token = newtoken;
      }
    },
    methods: {
        btnRegist: function (event) {
            requestProxy('/auth/register', {
                username: document.getElementById("reg_login").value,
                password: document.getElementById("reg_password").value,
                firstName: document.getElementById("reg_name").value,
                lastName: document.getElementById("reg_surname").value,
                middleName: document.getElementById("reg_sname").value,
                apartment: document.getElementById("reg_adress").value,
                phoneNumber: document.getElementById("reg_phone").value,
                email: document.getElementById("reg_email").value,
            },response => { alert('удачно');}, x => {alert('ошибка'); });
        },

        btnLogin: function (event) {
            requestProxy('/auth/login',{
                    username: document.getElementById("login").value,
                    password: document.getElementById("password").value,
                }, response => {
                    this.token = response.data.token;
                    this.state = loginedByUser;
                    this.currentWindow = 'setIndaction';
                },e => {
                    alert('ошибка');
                    console.error(e.message);
                },
                this.token
            );
        },

        btnSetIndaction: function () {
            requestProxy( '/meters/send',
                {
                    hotWater: document.getElementById("hotWater").value,
                    coldWater: document.getElementById("coldWater").value,
                    electricity: document.getElementById("light").value,
                },response => {}, e => {
                    alert('ошибка');
                    console.error(e.message);
                },
                this.token
            );
        },

        btnShowIndaction: function(){
            requestGetProxy( '/meters/?periodType=ALL',
                response => {
                    let grid = this.$refs.indications_grid;
                    grid.heroes = response.data;
                    grid.columns = ['electricity', 'hotWater', 'coldWater', 'date'];
                    grid.$forceUpdate();
                    //console.log(gridData);
                }, e => {
                    alert('ошибка');
                },
                this.token
            );
        }
    }
});