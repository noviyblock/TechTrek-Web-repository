import React, { useEffect, useState } from "react";
import SpeechRecognition, {
  useSpeechRecognition,
} from "react-speech-recognition";
import micro from "../../shared/micro.svg";
import { Color } from "../../shared/Color";
import ProfileButton from "../atom/ProfileButton";

//      await navigator.mediaDevices.getUserMedia({ audio: true });

export const CrisisDecision: React.FC<{
  onClick: () => void;
  inputValue: string;
  inputOnChange: (event: string) => void;
}> = ({ onClick, inputOnChange, inputValue }) => {
  const [isRecording, setIsRecording] = useState(false);

  const {
    transcript,
    resetTranscript,
    listening,
    browserSupportsSpeechRecognition,
  } = useSpeechRecognition();

  useEffect(() => {
    if (isRecording) {
      inputOnChange(transcript);
      console.log("Распознанный текст:", transcript);
    }
  }, [transcript, isRecording]);

  useEffect(() => {
    if (!listening && isRecording) {
      navigator.mediaDevices.getUserMedia({ audio: true });
      SpeechRecognition.startListening({
        continuous: true,
        language: "ru-RU",
        interimResults: true,
      });
    }
  }, [listening, isRecording]);

  const toggleRecording = async () => {
    if (isRecording) {
      SpeechRecognition.stopListening();
      setIsRecording(false);
      inputOnChange(transcript);
    } else {
      await navigator.mediaDevices.getUserMedia({ audio: true });
      resetTranscript();
      SpeechRecognition.startListening({
        continuous: true,
        language: "ru-RU",
        interimResults: true,
      });
      setIsRecording(true);
    }
  };

  if (!browserSupportsSpeechRecognition) {
    return <p>Ваш браузер не поддерживает распознавание речи.</p>;
  }

  return (
    <div
      className="flex flex-col flex-none p-5 text-white font-inter text-lg w-full gap-5 border-[1px] border-solid"
      style={{ backgroundColor: "#18181B", borderColor: "#3C3C3C" }}
    >
      <div>Укажите свое решение текстом или голосом</div>
      <div
        style={{
          position: "relative",
          backgroundColor: "#27272A",
        }}
        className="rounded-lg"
      >
        <textarea
          value={inputValue}
          onChange={(e) => inputOnChange(e.target.value)}
          rows={4}
          style={{
            width: "calc(100% - 38px)",
            resize: "none",
            boxSizing: "border-box",
            backgroundColor: "#27272A",
            outline: "none",
          }}
          className="font-inter text-white rounded-lg pl-3 pt-3 pb-3"
        />
        <div
          onClick={toggleRecording}
          style={{
            position: "absolute",
            right: "10px",
            top: "10px",
            background: isRecording ? Color["Primary"] : "#3F3F46",
            border: "none",
            borderRadius: "50%",
            width: "28px",
            height: "28px",
            color: "white",
            fontSize: "16px",
            cursor: "pointer",
          }}
          className="flex items-center justify-center"
        >
          <img src={micro} className="w-5 h-5"></img>
        </div>
      </div>
      <div className="flex justify-center items-center">
        <ProfileButton color={"Primary"} height={30} onClick={onClick}>
          Отправить
        </ProfileButton>
      </div>
    </div>
  );
};
export default CrisisDecision;
