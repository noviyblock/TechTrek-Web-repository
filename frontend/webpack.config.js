const path = require('path');

module.exports = {
  entry: './src/index.js', // Точка входа
  output: {
    filename: 'main.js', // Имя выходного файла
    path: path.resolve(__dirname, 'dist'), // Папка для выходных файлов
  },
  devServer: {
    static: './public', // Папка для статических файлов
    port: 5432, // Порт для разработки
  },
  module: {
    rules: [
      {
        test: /\.(js|jsx)$/, // Обрабатываем .js и .jsx файлы
        exclude: /node_modules/, // Исключаем папку node_modules
        use: {
          loader: 'babel-loader', // Используем babel-loader
        },
      },
      {
        test: /\.css$/, // Обрабатываем .css файлы
        use: ['style-loader', 'css-loader'],
      },
    ],
  },
  resolve: {
    extensions: ['.js', '.jsx'], // Добавляем поддержку .jsx
  },
};