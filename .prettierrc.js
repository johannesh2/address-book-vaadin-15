// prettier.config.js or .prettierrc.js
module.exports = {
  tabWidth: 2,
  semi: true,
  singleQuote: true,
  overrides: [
    {
      files: '*.java',
      options: {
        tabWidth: 4
      }
    }
  ]
};
