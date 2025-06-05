import React, { useState } from "react";
import { register, verify_otp } from "../../api/Auth";
import InputField from "../atom/InputField";
import Link from "../atom/Link";
import MediumButton from "../atom/MediumButton";
import { useNavigate } from "react-router-dom";

const ConfirmRegister: React.FC<{ width: string; email: string }> = ({
  width,
  email,
}) => {
  const [otp, setOtp] = useState("");
  const navigate = useNavigate();

  async function registerOnClick(e: React.FormEvent<HTMLFormElement>) {
    try {
      e.preventDefault();
      const status = await verify_otp({ email, otp });
      if (status && status >= 200 && status < 300) 
        navigate("/profile")
    } catch (error) {
      console.error(error);
    }
  }

  return (
    <div className="flex flex-col items-center pt-16">
      <div className="text-center font-inter" style={{ color: "#FFFFFF" }}>
        Введите код, который вам пришел на указанную почту
      </div>
      <div className="text-center text-white font-inter text-xl pt-12">
        {email}
      </div>
      <form
        className="flex flex-col items-center py-10 w-full"
        onSubmit={registerOnClick}
        id="registerForm"
      >
        <div className="flex flex-col gap-12 items-center w-full">
          <div className="flex flex-col gap-12 items-center w-full">
            <div className="flex flex-col gap-4 w-full items-center">
              <InputField placeholder="код" value={otp} onChange={setOtp} />
            </div>
            <Link href="/register">Изменить почту</Link> {/* back */}
          </div>
          <MediumButton color="Primary" form="registerForm">
            Подтвердить
          </MediumButton>
        </div>
      </form>
    </div>
  );
};

export default ConfirmRegister;
