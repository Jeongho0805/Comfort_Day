
const nickname = document.getElementById('nickname-input');
nickname.addEventListener('input', function() {
    const text = nickname.value;
    const error = document.getElementById('nickname-error');
    if (isNotValidateNickname(text)) {
        error.style.visibility = 'visible'
    } else {
        error.style.visibility = 'hidden'
    }
})

const isNotValidateNickname = (nickname) => {
    return nickname.length < 2 || nickname.length > 15;
}
