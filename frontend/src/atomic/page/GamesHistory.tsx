import { Game } from "../../api/services/UserService";
import { Color, defaultBlockColor } from "../../shared/Color";
import { categories } from "../../shared/constants";
import ColoredText from "../atom/ColoredText";
import MediumButton from "../atom/MediumButton";
import ProfileButton from "../atom/ProfileButton";
import SmallButton from "../atom/SmallButton";

const GamesHistory: React.FC<{ games: Game[] }> = ({ games }) => {
  return (
    <div className="flex flex-col gap-3 h-screen text-white">
      <div className="font-inter text-lg">История</div>
      <div
        className="overflow-y-auto h-screen"
        style={{ maxHeight: "calc(100% - 200px)" }}
      >
        <table className="w-full table-auto border-separate border-spacing-y-2 h-full">
          <thead
            className="sticky top-0"
            style={{ background: defaultBlockColor }}
          >
            <tr style={{ color: "#949494" }}>
              <th className="text-left p-2">Компания</th>
              <th className="text-left p-2">Сфера</th>
              <th className="text-left p-2">Балл</th>
              <th className="text-left p-2">
                <div className="flex justify-end mr-[74px]">Статус</div>
              </th>
            </tr>
          </thead>
          <tbody>
            {games.map((game, idx) => (
              <tr key={idx} className="" style={{ backgroundColor: "#18181B" }}>
                <td className="p-2">{game.companyName}</td>
                <td className="p-2">
                  <div className="flex justify-start">
                    <ColoredText
                      background={
                        categories.find((cat) => cat.name == game.sphere)
                          ?.color!
                      }
                      className="text-sm"
                    >
                      {game.sphere}
                    </ColoredText>
                  </div>
                </td>
                <td className="p-2">
                  {game.finalScore ? `${game.finalScore}/100` : "?"}
                </td>
                <td className="p-2">
                  <div className="flex justify-end text-base mr-6">
                    {game.finalScore === null ? (
                      <ProfileButton
                        color={"Secondary"}
                        height={33}
                        width="11em"
                      >
                        Возобновить игру
                      </ProfileButton>
                    ) : (
                      <div></div>
                    )}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default GamesHistory;
