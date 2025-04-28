import InputField from "../atom/InputField";
import Link from "../atom/Link";
import MediumButton from "../atom/MediumButton";

const RegistrationForm: React.FC<{ width: string }> = ({ width }) => (
  <div className="flex flex-col items-center pt-16">
    <div className="text-center font-inter" style={{ color: "#FFFFFF" }}>
      Добро пожаловать
      <br />в TechTrack!
    </div>
    <form className="flex flex-col items-center py-16 w-full">
      <div className="flex flex-col gap-12 items-center w-full">
        <div className="flex flex-col gap-12 items-center w-full">
          <div className="flex flex-col gap-4 w-full items-center">
            <InputField placeholder="e-mail" type="email" />
            <InputField placeholder="password" type="password" />
          </div>
          <Link href="">Регистрация</Link>
        </div>
        <MediumButton color="Primary">Войти</MediumButton>
      </div>
    </form>
  </div>
);

export default RegistrationForm;
