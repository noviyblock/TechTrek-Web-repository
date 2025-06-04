import { useState } from "react";
import ConfirmRegister from "../organism/ConfirmRegisterForm";
import RegistrationForm from "../organism/RegistrationForm";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";

const RegistrationPage: React.FC = () => {
  const [email, setEmail] = useState("");
  const [screen, setScreen] = useState<keyof typeof registerPages>("registration");

  const registerPages = {
    registration: <RegistrationForm width="33%" onClick={() => {setScreen("confirmation")}} email={email} setEmail={setEmail}/>,
    confirmation: <ConfirmRegister email={email} width="33%"/>,
  }

  return (
    <SingleElementLayoutTemplate backable={false} width="33%" minWidth="20em">
      {registerPages[screen]}
    </SingleElementLayoutTemplate>
  );
};

export default RegistrationPage;
