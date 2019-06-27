package userbase.write.event.events;

import userbase.write.event.commands.HotelDeleteCommand;

import javax.validation.constraints.NotNull;

public class HotelDeleted extends EventRoot {

    public HotelDeleted() {
        super();
    }

    public HotelDeleted(HotelDeleteCommand cmd) {
        super(cmd);
        this.id = cmd.getId();
    }


    @NotNull
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setId(String id) {
        this.id = Long.valueOf(id);
    }

}
