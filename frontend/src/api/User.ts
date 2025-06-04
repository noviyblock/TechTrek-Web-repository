import axiosInstance, { safeRequest } from "./AxiosInstance";
import UserService, { User } from "./services/UserService";

export const getUser = async (): Promise<User> =>
  safeRequest(() => axiosInstance.get<User>("/user/me"));
