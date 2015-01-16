package slicetool;

public class ImageMeta {

    private String mOrigin, mMoveFrom, mMoveTo;

    public ImageMeta(String origin) {
        mOrigin = origin;
        mMoveFrom = origin;
    }

    public String getOrigin() {
        return mOrigin;
    }

    public String getMoveFrom() {
        return mMoveFrom;
    }

    public String getMoveTo() {
        return mMoveTo;
    }

    public void setMoveFrom(String moveFrom) {
        mMoveFrom = moveFrom;
    }

    public void setMoveTo(String moveTo) {
        mMoveTo = moveTo;
    }

}
