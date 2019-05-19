package protocol;

public class Package {
    private final byte bMagic;
    private final byte bSrc;
    private final long bPktId;
    private final int wLen;
    private final short packageCrc16H;
    private final int cType;
    private final int bUserId;
    private final String message;
    private final short packageCrc16B;

    public byte getbMagic() {
        return bMagic;
    }

    public byte getbSrc() {
        return bSrc;
    }

    public long getbPktId() {
        return bPktId;
    }

    public int getwLen() {
        return wLen;
    }

    public short getPackageCrc16H() {
        return packageCrc16H;
    }

    public int getcType() {
        return cType;
    }

    public int getbUserId() {
        return bUserId;
    }

    public String getMessage() {
        return message;
    }

    public short getPackageCrc16B() {
        return packageCrc16B;
    }

    public Package(PackageBuilder builder) {
        this.bMagic = builder.bMagic;
        this.bSrc = builder.bSrc;
        this.bPktId = builder.bPktId;
        this.wLen = builder.wLen;
        this.packageCrc16H = builder.packageCrc16H;
        this.cType = builder.cType;
        this.bUserId = builder.bUserId;
        this.message = builder.message;
        this.packageCrc16B = builder.packageCrc16B;
    }

    public PackageBuilder builder() {
        return new PackageBuilder();
    }

    @Override
    public String toString() {
        return "Package{" +
                "bMagic=" + bMagic +
                ", bSrc=" + bSrc +
                ", bPktId=" + bPktId +
                ", wLen=" + wLen +
                ", packageCrc16H=" + packageCrc16H +
                ", cType=" + cType +
                ", bUserId=" + bUserId +
                ", message='" + message + '\'' +
                ", packageCrc16B=" + packageCrc16B +
                '}';
    }

    private static class PackageBuilder {


        private byte bMagic;
        private byte bSrc;
        private long bPktId;
        private int wLen;
        private short packageCrc16H;
        private int cType;
        private int bUserId;
        private String message;
        private short packageCrc16B;

        private PackageBuilder() { }

        public Package build() {
            return new Package(this);
        }

        public PackageBuilder withBMagic(byte bMagic) {
            this.bMagic = bMagic;
            return this;
        }

        public PackageBuilder withBSrc(byte bSrc) {
            this.bSrc = bSrc;
            return this;
        }

        public PackageBuilder withBPktId(long bPktId) {
            this.bPktId = bPktId;
            return this;
        }

        public PackageBuilder withWLen(int wLen) {
            this.wLen = wLen;
            return this;
        }

        public PackageBuilder withPackageCrc16H(short packageCrc16H) {
            this.packageCrc16H = packageCrc16H;
            return this;
        }

        public PackageBuilder withCType(int cType) {
            this.cType = cType;
            return this;
        }

        public PackageBuilder withBUserId(int bUserId) {
            this.bUserId = bUserId;
            return this;
        }

        public PackageBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public PackageBuilder withPackageCrc16B(short packageCrc16B) {
            this.packageCrc16B = packageCrc16B;
            return this;
        }
    }
}
