import TitledText from "../atom/TitledText";
import Block from "../molecule/Block";
import Logo from "../../shared/banana.jpeg";
import ProfileButton from "../atom/ProfileButton";
import ProfileButtonBorder from "../atom/ProfileButtonBorder";

const ProfileBlock: React.FC<{ username: string; email: string }> = ({
  username,
  email,
}) => (
  <div className="flex flex-col gap-1 basis-[20%] shrink-0 min-h-0 overflow-auto min-w-[185px]">
    <Block grow={1}>
      <div className="flex flex-col justify-between" style={{height: "100%"}}>
        <div className="flex flex-col gap-5">
          <div className="font-iner text-white">Профиль</div>
          <img src={Logo}></img>
          <TitledText header="Имя" text={username} />
          <TitledText header="Почта" text={email} />
        </div>
        {/* <div className="flex flex-col gap-5">
          <div className="font-iner text-white">Настройки</div>

          <ProfileButton height={32} color="DefaultAccent">
            Изменить пароль
          </ProfileButton>
        </div> */}
        <div className="flex flex-row gap-5">
          <div className="overflow-hidden w-2/3 flex-grow">
            <ProfileButtonBorder height={40} width={"100%"} color="Danger">
              Удалить аккаунт
            </ProfileButtonBorder>
          </div>
          <div className="overflow-hidden w-1/3 flex-grow">
            <ProfileButtonBorder height={40} width={"100%"} color="White">
              Выйти
            </ProfileButtonBorder>
          </div>
        </div>
      </div>
    </Block>
  </div>
);

export default ProfileBlock;
