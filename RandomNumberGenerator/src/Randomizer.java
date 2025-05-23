import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

/**
 * 随机数生成器GUI应用程序，支持十进制和十六进制随机数生成及相互转换
 */
public class Randomizer {
    public Randomizer(){
        createGUI();
    }
    public void createGUI(){
        JFrame frame = new JFrame("随机数生成器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel decimalPanel = createNumberSystemPanel(
            NumberSystem.DECIMAL,
            "十进制", 
            new String[]{"1","2","3","4","5","6","7","8","9","10"}, 
            new String[]{"1","3","5","10"}, 
            "生成随机数", 
            "转换为十六进制"
        );

        JPanel hexaPanel = createNumberSystemPanel(
            NumberSystem.HEXA,
            "十六进制", 
            new String[]{"1","2","4","8"}, 
            new String[]{"1","3","5","10"}, 
            "生成随机数", 
            "转换为十进制"
        );

        tabbedPane.addTab("十进制", decimalPanel);
        tabbedPane.addTab("十六进制", hexaPanel);

        frame.add(tabbedPane);
        //frame.pack();
        frame.setSize(550, 450);
        frame.setLocationRelativeTo(null); //窗口居中
        frame.setVisible(true);

    }

    /**
     * 数字系统枚举，标识当前面板的进制类型
     */
    enum NumberSystem{
        DECIMAL,
        HEXA
    }

    /**
     * 核心工厂方法：创建完整的数字系统操作面板
     * @param numberSystem 数字系统类型（DECIMAL/HEXA）
     * @param title 面板标题（显示在选项卡上）
     * @param digits 可用位数选项数组
     * @param counts 生成数量选项数组
     * @param generateText 生成按钮文本
     * @param convertText 转换按钮文本
     * @return 组装完成的数字系统操作面板
     */
    private JPanel createNumberSystemPanel(
        NumberSystem numberSystem,
        String title,
        String[] digits,
        String[] counts,
        String generateText,
        String convertText
    ){
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        // 创建位数列表
        JList<String> digitList = new JList<>(digits);
        digitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel digitPanel = createListPanel(digitList, "选择生成数的位数");

        // 创建数量列表
        JList<String> countList = new JList<>(counts);
        countList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel countPanel = createListPanel(countList, "选择要生成的数量");

        JPanel listPanel = createListContainer(digitPanel, countPanel);

        // 创建文本区域
        JTextArea generatedTextArea = new JTextArea();
        generatedTextArea.setEditable(false);
        generatedTextArea.setLineWrap(true);
        JScrollPane generatedScroll = new JScrollPane(generatedTextArea);
        JPanel generatedPanel = createTextAreaPanel(generatedScroll, "生成的" + title + "随机数");


        JTextArea convertedTextArea = new JTextArea();
        convertedTextArea.setEditable(false);
        convertedTextArea.setLineWrap(true);
        JScrollPane convertedScroll = new JScrollPane(convertedTextArea);
        JPanel convertedPanel = createTextAreaPanel(convertedScroll, "转换结果");

        JSplitPane textPanel = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, 
            generatedPanel,
            convertedPanel
        );
        textPanel.setResizeWeight(0.5);

        // 功能按钮面板
        JPanel buttonPanel = createButtonPanel(
            generateText, 
            convertText, 
            numberSystem, 
            digitList, 
            countList, 
            generatedTextArea, 
            convertedTextArea
        );

        // 组装主面板
        panel.add(listPanel, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * 创建带标签的文本区域面板
     */
    private JPanel createTextAreaPanel(JScrollPane scrollPane, String labelText){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(labelText),BorderLayout.NORTH);
        panel.add(scrollPane,BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建水平排列的列表容器
     * @param leftPanel 左侧面板（通常为位数选择）
     * @param rightPanel 右侧面板（通常为数量选择）
     * @return 使用GridLayout布局的容器面板
     */
    private JPanel createListContainer(JPanel leftPanel, JPanel rightPanel){
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        panel.add(leftPanel);
        panel.add(rightPanel);
        return panel;
    }

    /**
     * 创建标准化的列表面板
     * @param list 要显示的JList组件
     * @param title 面板标题
     * @return 包含标题和滚动条的标准列表容器
     */
    private JPanel createListPanel(JList<?> list, String title){
        JPanel panel = new JPanel(new BorderLayout());
        //统一内边距：上、左、下、右各10像素
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel(title), BorderLayout.NORTH);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);
        return panel;
    }

    /**
     * 创建功能按钮面板并绑定事件处理
     * @param generateText 生成按钮文本
     * @param convertText 转换按钮文本
     * @param numberSystem 当前数字系统类型
     * @param digitList 位数选择列表引用
     * @param countList 数量选择列表引用
     * @param generatedTextArea 生成的随机数显示区域
     * @param convertedTextArea 转换结果显示区域
     * @return 包含功能按钮的面板
     */
    private JPanel createButtonPanel(
        String generateText, 
        String convertText,
        NumberSystem numberSystem,
        JList<String> digitList,
        JList<String> countList,
        JTextArea generatedTextArea,
        JTextArea convertedTextArea
        ){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,50,10));
        JButton btnGenerate = new JButton(generateText);
        JButton btnConvert = new JButton(convertText);

        btnGenerate.addActionListener(e -> {
            String seletedDigit = digitList.getSelectedValue();
            String seletedCount = countList.getSelectedValue();

            //检查是否已选择选项
            if(seletedDigit == null || seletedCount == null){
                return;
            }

            try {
                int digit = Integer.parseInt(seletedDigit);
                int count = Integer.parseInt(seletedCount);
                Random r = new Random();
                String[] randomNumbers = new String[count];

                if(numberSystem == NumberSystem.DECIMAL){
                    //生成十进制随机数
                    long max = (long)Math.pow(10, digit);
                    for(int i = 0; i < count; i++){
                        long num = r.nextLong(max);
                        randomNumbers[i] = String.format("%0"+digit+"d", num);
                    }
                }else{
                    //生成十六进制随机数
                    long max = (long)Math.pow(16, digit);
                    for(int i = 0; i < count; i++){
                        long num = r.nextLong(max);
                        randomNumbers[i] = String.format("%0"+digit+"X", num);
                    }
                }
                generatedTextArea.setText(String.join("\n", randomNumbers));
            } catch (NumberFormatException ex) {
                generatedTextArea.setText("异常生成");
            }
        });

        btnConvert.addActionListener(e -> {
            String content = generatedTextArea.getText();
            if(content == null){
                return;
            }

            String[] numbers = content.split("\n");
            String[] convertedNumbers = new String[numbers.length];

            if(numberSystem == NumberSystem.DECIMAL){
                //十进制转十六进制
                for(int i = 0; i < numbers.length; i++){
                    try {
                        long num = Long.parseLong(numbers[i],10);
                        convertedNumbers[i] = Long.toHexString(num).toUpperCase();
                    } catch (NumberFormatException ex) {
                        convertedNumbers[i] = "转换失败" + numbers[i];
                    }
                }
            }else{
                //十六进制转十进制
                for(int i = 0; i < numbers.length; i++){
                    try {
                        long num = Long.parseLong(numbers[i],16);
                        convertedNumbers[i] = Long.toString(num);
                    } catch (NumberFormatException ex) {
                        convertedNumbers[i] = "转换失败" + numbers[i];
                    }
                }
            }
            convertedTextArea.setText(String.join("\n", convertedNumbers));
        });
        panel.add(btnGenerate);
        panel.add(btnConvert);
        return panel;
    }

    public static void main(String[] args) throws Exception {
        new Randomizer();
    }
}
