import CommandNameForm from "../organism/CommandNameForm";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";

const CommandNamePage: React.FC<{onClick: (commandName: string) => void}> = ({onClick}) => (
  <SingleElementLayoutTemplate backable={true} width="40%" minWidth="20em">
    <CommandNameForm width="33%" onClick={onClick} />
  </SingleElementLayoutTemplate>
);

export default CommandNamePage;
