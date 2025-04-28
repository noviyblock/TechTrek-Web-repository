import { LinkProps } from "../../shared/Types";

const Link: React.FC<LinkProps> = ({ children, href, color = "Default" }) => (
  <a href={href} className="text-white underline font-inter">
    {children}
  </a>
);
export default Link;
