import Vue from 'vue';
import Vuex from 'vuex';
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex);

const store = new Vuex.Store({
    plugins: [createPersistedState()], // 使用插件
    state: {
        token: null, // 初始时token为空
        isLoggedIn: false, // 是否登录的状态
        Role: null
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
            // 模拟登录操作，实际应用中需要向服务器发送登录请求
            commit('setToken', token);
            commit('setLoggedIn', true);
            commit('setRole', role);
        },
        logout({ commit }) {
            // 模拟登出操作
            commit('setToken', null);
            commit('setRole', null);
            commit('setLoggedIn', false);
        },
    },
    getters: {
        getToken: state => state.token,
        isLoggedIn: state => state.isLoggedIn,
        getRole: state => state.Role,
    },
});

export default store;
