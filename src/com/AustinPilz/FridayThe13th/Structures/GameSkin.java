package com.AustinPilz.FridayThe13th.Structures;

public enum GameSkin
{

    JASON("eyJ0aW1lc3RhbXAiOjE1MDAxODQ4NTQxODYsInByb2ZpbGVJZCI6ImQyZjBhYzQ2OWI0ZDRhMmI5NjYxODcyYmE2NWY5YWM5IiwicHJvZmlsZU5hbWUiOiJhdXN0aW5waWx6Iiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS82ODc0ZmFiN2JiM2I1ZjlmNjhhYjAzNmQ3ZDdhZGNhNmY4YWI2ZGZiNjJkN2YyZWFiNGQxZjBjMTc5OTRjNiJ9fX0=", "ccxMmVbsoT4NpG9H7p1wtNShKpnpw+Bf8lI9Ifo2A7OqeQOfnVuq7al8vUkGNJkNP9WvaPprhU5IriVFyV4F1SXPXqrsCSsZlZIRTp5qD8zCaSDlqdz5g7+ZVDhPsFTqphYjpjvP6479xUgJ2u2mH5GMCWne0KaPqCXOazQY416nVRcHgurTA3VjWw9JvDbnAXryoZGUUqfeaFxF4sXlF/L00Do/Jwn1sIfNWghOTWORtUwf+6b+CLdmpAwCiBvenA+eo1lEGqrC5EkS/k5t63KVyZTV7zc4N4xCm1H1EMzoG+9re3AhN3ojSMAYrXDMZz4epbHyzCiKL+clGP8dXdFvyRIKxxhuxqaESamft/8YUDXlPe3+pWz6m8loecmXSHXzi5ZulVa7VbueZOpsP9Wl+rXo8zMyNj3JHzef9Ql9RZifAVbQOy1eovVJ4GG+FQORG1IYiICbUJXjyl5LQ8qgTDPhyaPGhbrh0gVsmlxys3VXWs9NZdThjQgRTo0gNQnMpaPEfVFW2nUcIII9peLQwL61gaIY47bMO05G7/uKGp177fJJ+AgETcAvXyXYSPiXXeAmOgubPNEXKtArYqiGbAPa+7usMdXfIbOBohiWb/dEigUnZ+vGyafUJRWB0Man+znRkJhootVwkcDVZUrZDE6evbwv4/clHgKXK4Y="),
    ORIGINAL("", "");

    private String value, signature;

    GameSkin(String value, String signature) {
        this.value = value;
        this.signature = signature;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}