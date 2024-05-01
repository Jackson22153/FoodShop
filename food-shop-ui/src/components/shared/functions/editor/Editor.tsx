import { useRef } from 'react';
import SunEditor from 'suneditor-react';
import SunEditorCore from "suneditor/src/lib/core";
import 'suneditor/dist/css/suneditor.min.css';

interface Props{
  content: string,
  onChangeContent: (content: string)=> void,
  editable: boolean,
  height: string
}

export default function TextEditor(prop: Props) {
  const content = prop.content;
  const editable = prop.editable;
  const height = prop.height;
  // const [content, setContent] = useState("");

  const editor = useRef<SunEditorCore>()

  const getSunEditorInstance = (sunEditor: SunEditorCore) => {
    editor.current = sunEditor;
  };

  const onChangeContent = (content: string)=>{
    prop.onChangeContent(content)
    // setContent(content)
  }


  return (
    <div>
      <SunEditor getSunEditorInstance={getSunEditorInstance}
        height={height}
        setContents={content}
        onChange={onChangeContent}
        autoFocus={true}
        disable={!editable}
        setOptions={
          {
            buttonList: [
              [
                "bold",
                "underline",
                "italic",
                "strike",
                "list",
                "align",
                "fontSize",
                "formatBlock",
                "table",
                "image"
              ]
            ]
          }
        }
        disableToolbar={false}
      />
    </div>

  );
}
