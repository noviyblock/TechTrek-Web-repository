import LoginForm from "../organism/LoginForm";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";

const LoginPage: React.FC = () => (
  <SingleElementLayoutTemplate backable={false} width="33%" minWidth="20em">
    <LoginForm width="33%" />
  </SingleElementLayoutTemplate>
);

export default LoginPage;
