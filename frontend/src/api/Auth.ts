import axios from "axios";
import TokenService from "./services/TokenService";
import axiosInstance from "./AxiosInstance";
import UserService from "./services/UserService";

export interface Auth {
  accessToken: string;
  refreshToken: string;
}

export interface UserLogin {
  email: string;
  password: string;
}

export interface RefreshToken {
  refreshToken: string;
}

export interface UserRegister {
  username: string;
  password: string;
  email: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  username: string;
}

export interface StatusResponse {
  authorized: boolean;
  user: {
    id: number;
    name: string;
  };
}

export const refresh = () => {
  const refreshToken: RefreshToken = {
    refreshToken: TokenService.getRefreshToken()!,
  };
  return axios.post("/api/auth/refresh", refreshToken);
};

export const register = async (userRegister: UserRegister) => {
  try {
    const response = await axios.post<AuthResponse>(
      "/api/auth/register",
      userRegister
    );
    TokenService.setTokens(response.data);
    UserService.setUsername(response.data.username);
    return response.status
  } catch (error) {
    console.error(error);
    // todo handle error
  }
};

export const login = async (userLogin: UserLogin) => {
  try {
    const response = await axios.post<AuthResponse>(
      "/api/auth/login",
      userLogin
    );
    TokenService.setTokens(response.data);
    UserService.setUsername(response.data.username);
    return response.status;
  } catch (error) {
    //todo handle error
  }
};

export const status = async (): Promise<boolean> => {
  try {
    return (
      (await axiosInstance.get("/api/auth/status")).status === 200
    );
  } catch (error) {
    console.error(error);
    return false;
  }
};
