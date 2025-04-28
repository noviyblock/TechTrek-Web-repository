import RegistrationForm from "../organism/RegistrationForm";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";

const RegistrationPage: React.FC = () => (
  <SingleElementLayoutTemplate backable={false} width="33%" minWidth="20em">
    <RegistrationForm width="33%" />
  </SingleElementLayoutTemplate>
);

export default RegistrationPage;
