import CommandNameForm from "../organism/CommandNameForm";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";

const CommandNamePage: React.FC = () => (
  <SingleElementLayoutTemplate backable={true} width="40%" minWidth="20em">
    <CommandNameForm width="33%" />
  </SingleElementLayoutTemplate>
);

export default CommandNamePage;
