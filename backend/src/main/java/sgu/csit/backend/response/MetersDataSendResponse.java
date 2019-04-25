package sgu.csit.backend.response;

public class MetersDataSendResponse {
    private String electro;
    private String cold;
    private String hot;

    public MetersDataSendResponse(String electro, String cold, String hot) {
        this.electro = electro;
        this.cold = cold;
        this.hot = hot;
    }

    // getters
    public String getElectro() {
        return electro;
    }
    public String getCold() {
        return cold;
    }
    public String getHot() {
        return hot;
    }

    // setters
    public void setElectro(String electro) {
        this.electro = electro;
    }
    public void setCold(String cold) {
        this.cold = cold;
    }
    public void setHot(String hot) {
        this.hot = hot;
    }
}
