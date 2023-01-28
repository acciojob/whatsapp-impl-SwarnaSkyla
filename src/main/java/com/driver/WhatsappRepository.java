package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<String,User> userMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.userMap=new HashMap<String,User>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }
    public String createUser(String username,String mobile) throws Exception{

        if(!userMap.containsKey(mobile)){
            userMap.put(username,new User(username,mobile));
            return "SUCCESS";
        }
        throw new Exception("User already exists");


    }
    public Group createGroup(List<User> users){
        Group group;
//         customGroupCount++;

        if(users.size()==2){
            User admin=users.get(1);
            group= new Group(admin.getName(),users.size());
            groupUserMap.put(group,users);
        }
        else{
            customGroupCount+=1;
            group=new Group("Group "+customGroupCount,users.size());
            groupUserMap.put(group,users);
        }
        adminMap.put(group,users.get(0));
        return group;

    }
    public int createMessage(String content){
        this.messageId++;
        Message newmesage=new Message(messageId,content,new Date());
        return this.messageId;
    }
    public int sendMessage(Message message,User sender,Group group) throws Exception{
        if(!groupUserMap.containsKey(group)) {
            throw new Exception("Group does not exist");
        }
        if(!this.userIngroup(group,sender)){
            throw new Exception("You are not allowed to send message");
        }
        List<Message> msgs=new ArrayList<>();
        if(groupMessageMap.containsKey(group)){
            msgs=groupMessageMap.get(group);
        }
        msgs.add(message);
        groupMessageMap.put(group,msgs);
        return msgs.size();
    }
    public String changeAdmin(User approver,User user,Group group) throws Exception{
        if(!groupUserMap.containsKey(group)) throw new Exception("Group does not exist");
        if(!adminMap.get(group).equals(approver)){
            throw new Exception("Approver does not have rights");
        }
        if(!this.userIngroup(group,user)){
            throw new Exception("User is not a participant");
        }
        adminMap.put(group,user);
        return "SUCCESS";
    }
    public boolean userIngroup(Group group,User sender){
        List<User> u=groupUserMap.get(group);
        for(User user:u){
            if(user.equals(sender)){
                return true;
            }
        }
        return false;


    }
    public int removeUser(User user) throws Exception{

        if(userMap.containsValue(user)){

            userMap.remove(user);
        }
        throw new Exception("User not found");

    }
    public String findMessage(Date start, Date end,int K){
        return "Not searched";
    }
}
