import { Game } from "../../api/services/UserService";
import ProfileButton from "../atom/ProfileButton";
import ProfileButtonBorder from "../atom/ProfileButtonBorder";
import RegistrationForm from "../organism/RegistrationForm";
import SingleElementLayoutTemplate from "../template/SingleElementLayout";

const ProfileGame: React.FC = () => (
  <div className="flex flex-col gap-3">
    <div className="font-inter text-white text-lg">Игра</div>
    <div className="flex flex-row justify-between">
      <div>
        <ProfileButton color="Primary" width="7em" height={40}>
          Новая игра
        </ProfileButton>
      </div>
      <div className="flex flex-row gap-3">
        <ProfileButton height={40} width="12em">
          Правила игры
        </ProfileButton>
        <ProfileButtonBorder color="White" height={40}>
          Помощь
        </ProfileButtonBorder>
      </div>
    </div>
  </div>
);

export default ProfileGame;
