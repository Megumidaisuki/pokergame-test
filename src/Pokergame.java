import java.sql.SQLOutput;
import java.util.*;


public class Pokergame {
    static HashMap<Integer, Card> map = new HashMap<>();
    static List<Integer> list = new ArrayList<Integer>();

    static ArrayList<Player> players = new ArrayList<>();

    public Pokergame() {
        //创建玩家
        Player p11 = new Player("惠惠");
        Player p22 = new Player("阿库娅");
        Player p33 = new Player("达克妮斯");
        //构建牌库
        setCards();
        //开始游戏
        startgame(p11, p22, p33);
        //返回赢家
        Player winner = Play(players.listIterator(0).next(),
                players.listIterator(1).next(),
                players.listIterator(2).next());
        if (winner.getD()) {
            System.out.println("恭喜" + winner.getName() + "!地主胜利!");
        } else {
            System.out.println("恭喜" + winner.getName() + "!农名胜利!");
        }
    }

    public void setCards() {
        String[] colors = "♠-♥-♦-♣".split("-");
        String[] numbers = "3-4-5-6-7-8-9-10-J-Q-K-A-2".split("-");
        int serialnumber = 0;
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < colors.length; j++) {
                list.add((serialnumber));
                map.put(serialnumber++, new Card(numbers[i], colors[j]));
            }
        }
        list.add(serialnumber);
        map.put(serialnumber++, new Card("XIAO", ""));
        list.add(serialnumber);
        map.put(serialnumber, new Card("DA", ""));
        System.out.println("游戏规则：" + "1. 出牌时在下方输入牌面即可，不用输入花色。");
        System.out.println("\t\t" + "2. 每个单牌之间用空格连接。");
    }

    public static void startgame(Player p1, Player p2, Player p3) {
        Collections.shuffle(list);

        ArrayList<Integer> lord = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Integer poker = (Integer) list.get(i);
            if (i < 3) {
                lord.add(poker);
            }
            if (i >= 3) {
                if (i % 3 == 0) {
                    p1.putMyCards(poker);
                } else if (i % 3 == 1) {
                    p2.putMyCards(poker);
                } else {
                    p3.putMyCards(poker);
                }
            }
        }

        //抢地主
        Scanner sc = new Scanner(System.in);
        ArrayList<Player> p = new ArrayList<>();
        p.add(p1);
        p.add(p2);
        p.add(p3);
        for (Player player : p) {
            lookpoker(player.getMyCards());
            System.out.println(player.getName() + "您是否抢地主？（y/n）");
            String next1 = sc.nextLine();
            if (next1.equals("y")) {
                player.setD(true);      //给该玩家置地主位
                players.add(0, player);
                p.remove(player);
                player.getMyCards().addAll(lord);
                System.out.println(player.getName() + "是本局的地主");
                System.out.print("底牌:");
                lookpoker(lord);
                System.out.println();
                break;
            }
        }
        if (!p1.getD() && !p2.getD() && !p3.getD()) {
            System.out.println("没人叫地主，游戏结束");
            System.exit(0);
        }
        players.addAll(p);
    }

    public static Player Play(Player p1, Player p2, Player p3) {
        Scanner sc = new Scanner(System.in);
        //用于储存上家出的牌，下家出牌前必须与之比较
        ArrayList<Integer> precards = new ArrayList<>();
        //用于记录上家出牌类型
        StringBuilder preType = new StringBuilder();
        //记录出牌总次数
        int temp = 0;
        //记录空过次数，若连续两次空过，则清空precards
        int cotnull = 0;

        //玩家轮流出牌
        //判断手牌数，若为0，游戏结束，玩家胜利，结束方法
        while (true) {
            if (p1.getMyCards().size() == 0) {
                System.out.println("游戏结束!");
                return p1;
            } else if (p2.getMyCards().size() == 0) {
                System.out.println("游戏结束!");
                return p2;
            } else if (p3.getMyCards().size() == 0) {
                System.out.println("游戏结束!");
                return p3;
            }

            System.out.print("上家出的牌:");
            for (Integer precard : precards) {
                System.out.print(map.get(precard).getNum() + "");
            }
            System.out.println();
            System.out.println("上家的牌型:" + preType);

            if (temp % 3 == 0) {
                //p1玩家出牌
                System.out.println("你的牌是:");
                lookpoker(p1.getMyCards());
                System.out.println("现在是 " + p1.getName() + " 时间！(输入空格代表空过)");
                String next1 = sc.nextLine();
                //空过
                if (next1.equals(" ") && !precards.equals(null)) {
                    //空过判断数++
                    cotnull++;
                    //当空过判断数为2时，清空上个玩家的出牌
                    if (cotnull == 2) {
                        precards.clear();
                        preType.delete(0, preType.length());
                        cotnull = 0;
                        System.out.println("现在重新是你的回合!");
                    }
                    System.out.println("要不起");

                }
                //如果输入中存在两个空格，提示
                if (next1.contains("  ")) {
                    System.out.println("不能出现空格噢");
                }
                //通过judge方法判断出的牌是否符合规范，并更新precards和nowcards
                if (!next1.equals(" ")) {
                    if (!judge(next1, precards, preType, p1)) {
                        System.out.println("你出的牌有问题");
                    }
                    cotnull = 0;
                }
            } else if (temp % 3 == 1) {
                //p2玩家出牌
                System.out.println("你的牌是:");
                lookpoker(p2.getMyCards());
                System.out.println("现在是 " + p2.getName() + " 时间！(输入空格代表空过)");
                String next2 = sc.nextLine();
                //空过
                if (next2.equals(" ") && !precards.equals(null)) {
                    //空过判断数++
                    cotnull++;
                    //当空过判断数为2时，清空上个玩家的出牌
                    if (cotnull == 2) {
                        precards.clear();
                        preType.delete(0, preType.length());
                        cotnull = 0;
                        System.out.println("现在重新是你的回合!");
                    }
                    System.out.println("要不起");

                }
                //如果输入中存在两个空格，提示
                if (next2.contains("  ")) {
                    System.out.println("不能出现空格噢");
                }
                //通过judge方法判断出的牌是否符合规范，并更新precards和nowcards
                if (!next2.equals(" ")) {
                    if (!judge(next2, precards, preType, p2)) {
                        System.out.println("你出的牌有问题");
                    }
                    cotnull = 0;
                }
            } else if (temp % 3 == 2) {
                //p3玩家出牌
                System.out.println("你的牌是:");
                lookpoker(p3.getMyCards());
                System.out.println("现在是 " + p3.getName() + " 时间！(输入空格代表空过)");
                String next3 = sc.nextLine();
                //空过
                if (next3.equals(" ") && !precards.equals(null)) {
                    //空过判断数++
                    cotnull++;
                    //当空过判断数为2时，清空上个玩家的出牌
                    if (cotnull == 2) {
                        precards.clear();
                        preType.delete(0, preType.length());
                        cotnull = 0;
                        System.out.println("现在重新是你的回合!");
                    }
                    System.out.println("要不起");

                }
                //如果输入中存在两个空格，提示
                if (next3.contains("  ")) {
                    System.out.println("不能出现空格噢");
                }
                //通过judge方法判断出的牌是否符合规范，并更新precards和nowcards
                if (!next3.equals(" ")) {
                    if (!judge(next3, precards, preType, p3)) {
                        System.out.println("你出的牌有问题");
                    }
                    cotnull = 0;
                }

            }
            temp++;
        }
    }

    //创建一个方法，判断输入的牌是否符合规范，并更新precards和nowcards
    public static boolean judge(String next1, ArrayList<Integer> precards, StringBuilder preType, Player player) {
        //克隆一份玩家手牌
        ArrayList<Integer> myclone = (ArrayList<Integer>) player.getMyCards().clone();
        //1.将输入的字符串转换为权重存进
        //将输入的牌存进(打出牌)权重集合，用于与precards比较
        ArrayList<Integer> nowcards = new ArrayList<>();
        //用来存储当前的出牌类型
        String type = null;
        //将输入的牌分割成字符串数组
        String[] gotcards = next1.split(" ");
        //判断出的牌在手牌里是否有
        //判断出的牌手里是否有
        for (int i = 0; i < gotcards.length; i++) {
            boolean b = false;      //用于判断出的牌是否手牌里有
            for (Integer integer : myclone) {
                b = map.get(integer).getNum().equals(gotcards[i]);//从手牌中查找此牌面的权重
                if (b) {
                    myclone.remove(integer);
                    nowcards.add(integer);  //将权重存入出牌区
                    break;
                }
            }
            //此处将b运用的十分巧妙，若在里循环break，i1索引会在b==true处停止，下面的!b代码不会执行
            //而若匹配不到，里循环会在最后一个i1结束，也自然符合!b
            if (!b) {
                System.out.println("错误的，您输入的牌在手牌里找不到捏");
                return false;
            }
        }
        //将输入权重排序并转化为字符串对象
        next1 = shufc(nowcards);

        //根据有序的字符串判断牌型
        type = typejudge(next1);
        System.out.println("您出的牌型是:" + type);
        System.out.println("------------------------------------------------------");
        //验证出的牌是否符合规格
        if (type.equals("ERROR")) {
            //规则不符，检验失败
            System.out.println("出的牌不符合规格");
            return false;
        }
        //上家若出牌
        if (!precards.isEmpty()) {
            if (preType.toString().equals("王炸")) {
                //上家牌是王炸，检验失败
                System.out.println("王炸无敌，你出的牌没用");
                return false;
            }
            if (!type.equals("王炸") && !type.equals("炸弹")) {
                if (!type.equals(preType.toString()) || nowcards.size() != precards.size()) {
                    System.out.println("你出的牌和上家牌型不符");
                    //牌型不符，检验失败
                    return false;
                }
            }
            //根据牌大小判断
            if (!rankjudege(type, next1/*出的牌的字符串*/, nowcards/*出的牌的权重*/, String.valueOf(preType), precards)) {
                System.out.println("出的牌没上家大");
                return false;
            }
        }
        player.changeMyCards(myclone);
        precards.clear();
        //更新上家出的牌
        precards.addAll(nowcards);
        preType.delete(0, preType.length());
        preType.append(type);
        /*没必要的声明
        List<Integer> objectMy = player.getMyCards();
        List<Integer> objectNow = nowcards;
        System.out.println("手牌:" + objectMy);
        System.out.println("出的牌" + objectNow);
        */
        return true;
    }

    //按斗地主顺序码牌并生成新的next字符串
    public static boolean rankjudege(String type, String next1, ArrayList<Integer> nowcards, String preType, ArrayList<Integer> precards) {
        //单判断
        if (type.equals("单")) {
            if (nowcards.get(0) < precards.get(0) || map.get(precards.get(0)).getNum().equals(next1)) {
                return false;
            } else return true;
        }
        //对判断
        if (type.equals("对")) {
            if (nowcards.get(0) < precards.get(0) || map.get(precards.get(0)).getNum().equals(next1.substring(0, 1))) {
                return false;
            } else return true;
        }
        //三带判断
        if (type.equals("三带")) {
            if (nowcards.get(0) < precards.get(0)) {
                return false;
            } else return true;
        }
        //三带一判断
        if (type.equals("三带一")) {
            if (nowcards.get(0) < precards.get(0)) {
                return false;
            } else return true;
        }
        //三带二判断
        if (type.equals("三带二")) {
            if (nowcards.get(0) < precards.get(0)) {
                return false;
            } else return true;
        }
        //广义飞机判断
        //注：当飞机的最大机翅膀大于precards的最大机翅膀时，即视为此飞机更大
        if (type.equals("飞机无翅膀") || type.equals("飞机单翅膀") || type.equals("飞机对翅膀")) {
            if (nowcards.get(0) < precards.get(0)) {
                return false;
            } else return true;
        }
        //连对
        if (type.equals("连对")) {
            if (nowcards.get(0) < precards.get(0) || map.get(precards.get(0)).getNum().equals(next1.substring(0, 1))) {
                return false;
            } else return true;
        }
        //顺子
        if (type.equals("顺子")) {
            if (nowcards.get(0) < precards.get(0) || map.get(precards.get(0)).getNum().equals(next1.substring(0, 1))) {
                return false;
            } else return true;
        }
        //四带二
        if (type.equals("四带二")) {
            if (nowcards.get(0) < precards.get(0)) {
                return false;
            } else return true;
        }
        //炸弹
        if (type.equals("炸弹")) {
            if (precards.equals("炸弹")) {
                if (nowcards.get(0) < precards.get(0)) {
                    return false;
                } else return true;
            } else if (!precards.equals("王炸")) {
                return true;
            }
        }
        if (type.equals("王炸")) {
            return true;
        }
        return false;
    }

    public static String typejudge(String next1) {
        //字符串分割
        String[] split = next1.split(" ");
        //定义广义顺子
        String linkcards1 = "34567890JQKA";
        //定义广义连对
        String linkcards2 = "3344556677889900JJQQKKAA";
        //定义广义飞机
        String linkcards3 = "333444555666777888999000JJJQQQKKKAAA";
        //去掉字符串中的空格
        //将每个牌面转化为单个字母
        next1 = next1.replace(" ", "");
        next1 = next1.replace("10", "0");
        next1 = next1.replace("DA", "D");
        next1 = next1.replace("XIAO", "X");

        //接下来开始初始化规则

        if (next1.contains("D") && next1.contains("X") && next1.length() != 2) {
            //不是王炸
            return "ERROR";
        }
        if (split.length == 1) {
            return "单";
        } else if (split.length == 2) {
            if ((split[0] + split[1]).contains("DA") && (split[0] + split[1]).contains("XIAO")) {
                return "王炸";
            }
            if (split[0].equals(split[1])) {
                return "对";
            }
        } else if (split.length == 3) {
            if (split[0].equals(split[1]) && split[0].equals(split[2])) {
                return "三带";
            }
        } else if (split.length == 4) {
            if (split[0].equals(split[1]) && split[0].equals(split[2]) && split[0].equals(split[3])) {
                return "炸弹";
            }
            if (next1.matches("(.)\\1{2}(.)")) {
                return "三带一";
            }
        } else if (split.length == 5) {
            if (next1.matches("(.)\\1{2}(.)\\2")) {
                return "三带二";
            }
            if (linkcards1.contains(next1)) {
                return "顺子";
            }
        } else if (split.length > 5) {
            if (linkcards1.contains(next1)) {
                return "顺子";
            }
            if (next1.matches("(.)\\1{2}(.)\\2{2}.*")) {
                if (split.length % 3 == 0 && linkcards3.contains(next1)) {
                    return "飞机无翅膀";
                }
                if (next1.length() == 8 && next1.matches("(.)\\1{2}(.)\\2{2}.{2}")) {
                    if (!split[split.length - 1].equals(split[split.length - 2])) {
                        String substring = next1.substring(0, split.length - 3);
                        if (linkcards3.contains(substring)) {
                            return "飞机单翅膀";
                        }
                    }
                }
                if (next1.length() == 16 && next1.matches("(.)\\1{2}(.)\\2{2}(.)\\3{2}(.)\\4{2}.{4}")) {
                    if (!split[split.length - 1].equals(split[split.length - 2]) && !split[split.length - 3].equals(split[split.length - 2]) && !split[split.length - 4].equals(split[split.length - 3])) {
                        String substring = next1.substring(0, split.length - 5);
                        if (linkcards3.contains(substring)) {
                            return "飞机单翅膀";
                        }
                    }
                }
                if (next1.length() == 12 && next1.matches("(.)\\1{2}(.)\\2{2}(.)\\3{2}.{3}")) {
                    if (!split[split.length - 1].equals(split[split.length - 2]) && !split[split.length - 3].equals(split[split.length - 2])) {
                        String substring = next1.substring(0, split.length - 4);
                        if (linkcards3.contains(substring)) {
                            return "飞机单翅膀";
                        }
                    }
                }
                if (next1.length() == 10 && next1.matches("(.)\\1{2}(.)\\2{2}(.)\\3(.)\\4")) {
                    if (!split[split.length - 1].equals(split[split.length - 3])) {
                        String substring = next1.substring(0, split.length - 5);
                        if (linkcards3.contains(substring)) {
                            return "飞机对翅膀";
                        }
                    }
                }
                if (next1.length() == 15 && next1.matches("(.)\\1{2}(.)\\2{2}(.)\\3{2}(.)\\4{1}(.)\\5{1}(.)\\6{1}")) {
                    if (!split[split.length - 1].equals(split[split.length - 3]) && !split[split.length - 3].equals(split[split.length - 5])) {
                        String substring = next1.substring(0, split.length - 7);
                        if (linkcards3.contains(substring)) {
                            return "飞机对翅膀";
                        }
                    }
                }
                if (next1.length() == 20 && next1.matches("(.)\\1{2}(.)\\2{2}(.)\\3{1}(.)\\4{1}")) {
                    if (!split[split.length - 1].equals(split[split.length - 3]) &&
                            !split[split.length - 3].equals(split[split.length - 5]) &&
                            !split[split.length - 5].equals(split[split.length - 7])) {
                        String substring = next1.substring(0, split.length - 9);
                        if (linkcards3.contains(substring)) {
                            return "飞机对翅膀";
                        }
                    }
                }
            }
            if (linkcards2.contains(next1)) {
                if (split[0].equals(split[1]) && split.length % 2 == 0 && split[split.length - 1].equals(split[split.length - 2])) {
                    return "连对";
                }
            }
            if (next1.matches("(.)\\1{3}.{2}")) {
                if (!split[split.length - 1].equals(split[split.length - 2])) {
                    return "四带二";
                }
            }
            if (next1.matches("(.)\\1{3}(.)\\2{1}(.)\\3{1}")) {
                if (!split[split.length - 1].equals(split[split.length - 3])) {
                    return "四带四";
                }
            }
        }
        return "ERROR";
    }

    private static String shufc(ArrayList<Integer> nowcards) {
        //给出的牌排序
        Collections.sort(nowcards);
        StringBuilder sb = new StringBuilder();
        //通过map将权重转化为牌面
        for (Integer nowcard : nowcards) {
            sb.append(map.get(nowcard).getNum()).append(" ");
        }
        //将牌面从sb类型转化为String类型
        String next = "" + sb;
        next.trim();
        //将字符串类型值计数
        String[] split = next.split(" ");
        //以牌的牌面为key，出现次数为value，构造map集合
        HashMap<String, Integer> map1 = new HashMap<>();
        int integer;
        for (String s : split) {
            if (map1.containsKey(s)) {
                integer = map1.get(s) + 1;
                map1.put(s, integer);
            } else {
                map1.put(s, 1);
            }
        }
        //通过Arraylist构造函数把map.entrySet()转化为list(map没有按值排序的方法，我们要把map转化为list)
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map1.entrySet());
        entries.sort((o1, o2) -> {
            Integer key1 = null;
            Integer key2 = null;
            //如果出现次数相等按权重排序
            if (o1.getValue().equals(o2.getValue())) {
                //按权重排序
                Set<Map.Entry<Integer, Card>> entries1 = map.entrySet();
                for (Map.Entry<Integer, Card> integerCardEntry : entries1) {
                    if (integerCardEntry.getValue().getNum().equals(o1.getKey())) {
                        key1 = integerCardEntry.getKey();
                    }
                    if (integerCardEntry.getValue().getNum().equals(o2.getKey())) {
                        key2 = integerCardEntry.getKey();
                    }
                }
                return key1 > key2 ? -1 : 1;

            }
            //若出现次数不同
            //按出现次数从大到小排列
            return (o2.getValue() - o1.getValue());
        });
        //将排好序后的entries(有关牌名和出现次数的键值对集合)转化为String类型返回
        StringBuilder sp = new StringBuilder();
        for (Map.Entry<String, Integer> entry : entries) {
            for (int i = 0; i < entry.getValue(); i++) {
                sp.append(entry.getKey()).append(" ");
            }
        }
        next = sp.toString().trim();
        System.out.println("您出的牌是：" + next);
        return next;
    }

    //输出手中牌
    public static void lookpoker(ArrayList<Integer> mycards) {
        Collections.sort(mycards);
        System.out.print("[");
        for (Integer card : mycards) {
            System.out.print(map.get(card).getColor() + map.get(card).getNum() + "   ");
        }
        System.out.println("]");
    }
}
