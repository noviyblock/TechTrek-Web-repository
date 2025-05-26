import React, { useState } from "react";
import { register } from "../../api/Auth";
import InputField from "../atom/InputField";
import Link from "../atom/Link";
import MediumButton from "../atom/MediumButton";
import { useNavigate } from "react-router-dom";

const RegistrationForm: React.FC<{ width: string }> = ({ width }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [repeatedPassword, setRepeatedPassword] = useState("");
  const navigate = useNavigate();

  async function registerOnClick(e: React.FormEvent<HTMLFormElement>) {
    try {
      e.preventDefault();
      const response = await register({ username, email, password });
      if (response === 200) {
        navigate("/profile");
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
        onSubmit={registerOnClick}
        id="registerForm"
      >
        <div className="flex flex-col gap-12 items-center w-full">
          <div className="flex flex-col gap-12 items-center w-full">
            <div className="flex flex-col gap-4 w-full items-center">
              <InputField
                placeholder="username"
                value={username}
                onChange={setUsername}
              />
              <InputField
                placeholder="e-mail"
                type="email"
                value={email}
                onChange={setEmail}
              />
              <InputField
                placeholder="пароль"
                type="password"
                value={password}
                onChange={setPassword}
              />
              <InputField
                placeholder="подтвердите пароль"
                type="password"
                value={repeatedPassword}
                onChange={setRepeatedPassword}
              />
            </div>
            <Link href="/login">Вход</Link>
          </div>
          <MediumButton color="Primary" form="registerForm">
            Зарегестрироваться
          </MediumButton>
        </div>
      </form>
    </div>
  );
};

export default RegistrationForm;
