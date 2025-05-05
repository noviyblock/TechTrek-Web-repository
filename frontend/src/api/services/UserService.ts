enum UserFields {
  username = "username",
  email = "email"
}

export interface Game {
    sphere: string;
    finalScore: string;
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
    }
};

export default UserService;
