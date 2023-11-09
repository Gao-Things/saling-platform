import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

const store = new Vuex.Store({
    plugins: [createPersistedState()], // 持久化插件，存入localstorage
    state: {
        token: null, // 初始时token为空
        isLoggedIn: false, // 是否登录的状态
        Role: null,
        // 为admin和superAdmin的Aside分别赋值
        adminSidebarLinks: [
            { index: '/Home', icon: 'el-icon-s-home', title: 'Main' },
            { index: '/fun', icon: 'el-icon-alarm-clock', title: 'fun' },
            { index: '/customer', icon: 'el-icon-aim', title: 'customer' }
        ],
        superAdminSidebarLinks: [
            { index: '/superadmin/data', icon: 'el-icon-s-home', title: 'Main' },
            { index: '/admin', icon: 'el-icon-s-goods', title: 'Product'  },
            { index: '/superadmin/user', icon: 'el-icon-user-solid', title: 'User' },
            { index: '/superadmin/setting', icon: 'el-icon-s-tools', title: 'Setting' },
        ]
    },
    mutations: {
        setToken(state, token) {
            state.token = token;
        },
        setLoggedIn(state, value) {
            state.isLoggedIn = value;
        },
        setRole(state, value) {
            state.Role = value;
        },
    },
    actions: {
        login({ commit }, { token, role }) { // 使用对象参数
            commit('setToken', token);
            commit('setLoggedIn', true);
            commit('setRole', role);
        },
        logout({ commit }) {
            commit('setToken', null);
            commit('setRole', null);
            commit('setLoggedIn', false);
        },
    },
    getters: {
        getToken: state => state.token,
        isLoggedIn: state => state.isLoggedIn,
        getRole: state => state.Role,
        sidebarLinks(state) {
            return state.Role === 'ADMIN' ? state.adminSidebarLinks : state.superAdminSidebarLinks;
        }
    },
});

export default store;
