const HeadedBlock: React.FC<{ header: string; children: React.ReactNode }> = ({
  header,
  children,
}) => (
  <div
    className="flex flex-col p-4 gap-3 border border-solid text-white overflow-hidden flex-1 max-h-80"
    style={{ background: "#18181B", borderColor: "#3C3C3C", }}
  >
    <b className="font-inter">{header}</b>
    <div className="font-inter text-sm flex-1 overflow-auto" style={{}}>{children}</div>
  </div>
);
export default HeadedBlock;
