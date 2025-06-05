enum UserFields {
  username = "username",
  email = "email",
  temp = "temp",
}

export interface Game {
  id: number;
  sphere: string;
  finalScore: string;
  companyName: string;
}

export interface User {
  username: string;
  email: string;
  games: Game[];
}

const UserService = {
  setUsername: (username: string) => {
    localStorage.setItem(UserFields.username, username);
  },
  getUsername: () => {
    return localStorage.getItem(UserFields.username);
  },
  setTemp: (isTemp: string) => {
    localStorage.setItem(UserFields.temp, isTemp);
  },
  getTemp: () => localStorage.getItem(UserFields.temp),
};

export default UserService;
