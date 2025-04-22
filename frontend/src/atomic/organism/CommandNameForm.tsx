import InputField from "../atom/InputField";
import SmallButton from "../atom/SmallButton";

const CommandNameForm: React.FC<{ width: string }> = ({ width }) => (
  <div className="flex flex-col items-center pt-16">
    <div className="text-center font-inter" style={{ color: "#FFFFFF" }}>
    Отлично! Придумайте <br/> название для вашей компании
    </div>
    <form className="flex flex-col items-center py-16 w-full">
      <div className="flex flex-col gap-12 items-center w-full">
        <div className="flex flex-col gap-12 items-center w-full">
          <div className="flex flex-col gap-4 w-full items-center">
            <InputField placeholder="компания..." />
          </div>
        </div>
        <SmallButton color="Primary">Начать игру</SmallButton>
      </div>
    </form>
  </div>
);

export default CommandNameForm;
