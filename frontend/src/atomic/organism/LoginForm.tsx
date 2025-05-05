import { useState } from "react";
import InputField from "../atom/InputField";
import Link from "../atom/Link";
import MediumButton from "../atom/MediumButton";
import { login } from "../../api/Auth";
import { useNavigate } from "react-router-dom";

const LoginForm: React.FC<{ width: string }> = ({ width }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  async function loginOnClick(e: React.FormEvent<HTMLFormElement>) {
    try {
      e.preventDefault();
      const response = await login({ email, password });
      if (response === 200) {
        navigate("/game");
      }
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <div className="flex flex-col items-center pt-16">
      <div className="text-center font-inter" style={{ color: "#FFFFFF" }}>
        Добро пожаловать
        <br />в TechTrack!
      </div>
      <form
        className="flex flex-col items-center py-16 w-full"
        onSubmit={loginOnClick}
        id="loginForm"
      >
        <div className="flex flex-col gap-12 items-center w-full">
          <div className="flex flex-col gap-12 items-center w-full">
            <div className="flex flex-col gap-4 w-full items-center">
              <InputField
                placeholder="email"
                value={email}
                onChange={setEmail}
              />
              <InputField
                placeholder="пароль"
                type="password"
                value={password}
                onChange={setPassword}
              />
            </div>
            <Link href="/register">Регистрация</Link>
          </div>
          <MediumButton color="Primary" form="loginForm">
            Войти
          </MediumButton>
        </div>
      </form>
    </div>
  );
};

export default LoginForm;
