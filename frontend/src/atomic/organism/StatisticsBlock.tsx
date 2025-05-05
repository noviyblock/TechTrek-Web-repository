import { StatisticsProps } from "../../shared/Types";
import Labeled from "../atom/Labeled";
import Block from "../molecule/Block";
import PictureList from "../molecule/PictureList";
import PictureListStacked from "../molecule/PictureListStacked";
import TitledProgress from "../molecule/TitledProgress";

const Statistics: React.FC<StatisticsProps> = ({
  techReadiness,
  productReadiness,
  motivation,
  reputation,

  modificators,
  employees,
  actives,
}) => (
  <Block grow={1}>
    <div className="flex flex-col gap-3">
      <div className="flex flex-col gap-3">
        <div className="font-inter text-white text-base">Показатели</div>
        <div className="flex flex-col gap-1">
          <TitledProgress
            title="Техническая готовность"
            textColor="White"
            end={100}
            current={techReadiness}
            color="Primary"
            changeColor={true}
          />
          <TitledProgress
            title="Продуктовая готовность"
            textColor="White"
            end={100}
            current={productReadiness}
            color="Primary"
            changeColor={true}
          />
          <TitledProgress
            title="Мотивация"
            textColor="White"
            end={100}
            current={motivation}
            color="Primary"
            changeColor={true}
          />
          <TitledProgress
            title="Репутация"
            textColor="White"
            end={100}
            current={reputation}
            color="Primary"
            changeColor={true}
          />
        </div>
      </div>

      <div className="flex flex-col gap-3">
        <div className="font-inter text-white text-base">Модификаторы</div>
        <PictureList
          pictures={modificators.flatMap((value) => ({
            image: value.picture,
            rounding: 9999,
          }))}
          size={40}
        />
      </div>

      <div className="flex flex-col gap-3">
        <div className="font-inter text-white text-base">Сотрудники</div>
        <div className="flex gap-2">
          <Labeled label="Аналитики">
            <PictureListStacked
              stackedPictures={employees.flatMap((value) => ({
                image: value.picture,
                rounding: 9999,
              }))}
              size={30}
            />
          </Labeled>
          <Labeled label="Разработчики">
            <PictureListStacked
              stackedPictures={employees
                .flatMap((value) => ({ image: value.picture, rounding: 9999 }))
                .slice(1)}
              size={30}
            />
          </Labeled>
        </div>
      </div>

      <div className="flex flex-col gap-3">
        <div className="font-inter text-white text-base">Активы</div>
        <div className="flex gap-2">
          <Labeled label="Офис">
            <PictureListStacked
              stackedPictures={actives.flatMap((value) => ({
                image: value.picture,
                rounding: 9999,
              }))}
              size={30}
            />
          </Labeled>
        </div>
      </div>
    </div>
  </Block>
);

export default Statistics;
