package messenger_project.catchmindtalk.Item;


public class ChatMessageItem {

    public int Type;
    public String UserId;
    public String Nickname;
    public String Profile;
    public String Content;
    public long Time;



    public ChatMessageItem(int type,String userId, String nickname, String profile, String content, long time){

        this.Type = type;
        this.UserId = userId;
        this.Nickname = nickname;
        this.Profile = profile;
        this.Content = content;
        this.Time =  time;

    }


    public void setType(int type) { this.Type = type; }

    public void setUserId(String userId) { this.UserId = userId ; }

    public void setNickname(String nickname) { this.Nickname = nickname ; }

    public void setProfile(String profile) { this.Profile = profile ; }

    public void setContent(String content) {
        this.Content = content ;
    }

    public void setTime(long time) { this.Time = time ; }



    public int getType() { return this.Type; }

    public String getUserId() { return this.UserId; }

    public String getNickname() { return this.Nickname; }

    public String getProfile() { return this.Profile;}

    public String getContent() {
        return this.Content;
    }

    public long getTime() {
        return this.Time;
    }


}
