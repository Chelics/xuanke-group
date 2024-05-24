/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
  root: true,
  'extends': [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/eslint-config-typescript'
  ],
  parserOptions: {
    ecmaVersion: 'latest'
  },
  rules:{
    "vue/multi-word-component-names": ["error",{
      "ignores": ["Classroom","Login","Register","Sidebar","State"],
       //在这个数组中加入需要忽略的组件名
   }]
  }
}
