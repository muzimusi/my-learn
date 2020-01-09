package me.arjenlee.mapreducelearn;



import org.apache.commons.collections.CollectionUtils;

import java.io.File;
import java.util.*;

/**
 * A:B,C,D,F,E,O
 * B:A,C,E,K
 * C:F,A,D,I
 * D:A,E,F,L
 * E:B,C,D,M,L
 * F:A,B,C,D,E,O,M
 * G:A,C,D,E,F
 * H:A,C,D,E,O
 * I:A,O
 * J:B,O
 * K:A,C,D
 * L:D,E,F
 * M:E,F,G
 * O:A,H,I,J
 */
public class CommonFriendsJava {
    public static void main(String[] args) throws Exception {
        // map 阶段
        // 读入 A:B,C,D,F,E,O 拆分：B-A C-A D-A F-A E-A 0-A
        // 读入 B:A,C,E,K 拆分：A-B C-B E-B K-B
        // reduce 阶段
        // 聚合相同的key A:[B] B:[A] C:[A B] D:[A] E:[A B] F:[A] K:[B] O:[A]
        // 结论：[A B]共同好友：C
        Scanner scanner = new Scanner(new File("D:\\bigDatas\\inputs\\commonfriends\\friends.txt"));
        Map<String, List<String>> friendsDirectPeople = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] split = line.split(":");
            String people = split[0];
            String[] friendArray = split[1].split(",");
            for (String friend : friendArray) {
                List<String> peopleList = friendsDirectPeople.get(friend);
                if (CollectionUtils.isEmpty(peopleList)) {
                    peopleList = new ArrayList<>();
                }
                peopleList.add(people);
                friendsDirectPeople.put(friend, peopleList);
            }
        }
        Map<String, List<String>> commonFriendsMap = new TreeMap<>();//排序
        for (Map.Entry<String, List<String>> friend2PeopleListEntry : friendsDirectPeople.entrySet()) {
            //System.out.println(friend2PeopleListEntry.getKey() + ":" + friend2PeopleListEntry.getValue());
            List<String> peopleList = friend2PeopleListEntry.getValue();
            for (int i = 0; i < peopleList.size(); i++) {
                for (int j = i + 1; j + 1 < peopleList.size(); j++) {
                    String commonFriendKey = "(" + peopleList.get(i) + "," + peopleList.get(j) + ")";
                    List<String> commonFriendList = commonFriendsMap.get(commonFriendKey);
                    if (CollectionUtils.isEmpty(commonFriendList))
                        commonFriendList = new ArrayList<>();
                    commonFriendList.add(friend2PeopleListEntry.getKey());
                    commonFriendsMap.put(commonFriendKey, commonFriendList);
                }
            }
        }

        for (Map.Entry<String, List<String>> commonFriendEntry : commonFriendsMap.entrySet()) {
            System.out.println(commonFriendEntry.getKey() + ":" + commonFriendEntry.getValue());
        }
    }
}
