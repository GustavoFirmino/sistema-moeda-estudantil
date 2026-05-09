/* Moeda Estudantil — main.js */

document.addEventListener('DOMContentLoaded', () => {

  /* ---- Bootstrap form validation ---- */
  document.querySelectorAll('.needs-validation').forEach(form => {
    form.addEventListener('submit', e => {
      if (!form.checkValidity()) {
        e.preventDefault();
        e.stopPropagation();
      }
      form.classList.add('was-validated');
    });
  });

  /* ---- Auto-dismiss alerts after 5 s ---- */
  document.querySelectorAll('.alert.alert-dismissible').forEach(alert => {
    setTimeout(() => {
      const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
      bsAlert.close();
    }, 5000);
  });

  /* ---- Toggle password visibility ---- */
  const toggleBtn = document.getElementById('togglePassword');
  const passwordInput = document.getElementById('password');
  const toggleIcon = document.getElementById('toggleIcon');
  if (toggleBtn && passwordInput) {
    toggleBtn.addEventListener('click', () => {
      const isPassword = passwordInput.type === 'password';
      passwordInput.type = isPassword ? 'text' : 'password';
      toggleIcon.classList.toggle('fa-eye', !isPassword);
      toggleIcon.classList.toggle('fa-eye-slash', isPassword);
    });
  }

  /* ---- CPF mask ---- */
  document.querySelectorAll('input[placeholder="000.000.000-00"]').forEach(input => {
    input.addEventListener('input', () => {
      let v = input.value.replace(/\D/g, '').slice(0, 11);
      if (v.length > 9) v = v.replace(/^(\d{3})(\d{3})(\d{3})(\d{0,2})/, '$1.$2.$3-$4');
      else if (v.length > 6) v = v.replace(/^(\d{3})(\d{3})(\d{0,3})/, '$1.$2.$3');
      else if (v.length > 3) v = v.replace(/^(\d{3})(\d{0,3})/, '$1.$2');
      input.value = v;
    });
  });

  /* ---- CNPJ mask ---- */
  document.querySelectorAll('input[placeholder="00.000.000/0000-00"]').forEach(input => {
    input.addEventListener('input', () => {
      let v = input.value.replace(/\D/g, '').slice(0, 14);
      if (v.length > 12) v = v.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{0,2})/, '$1.$2.$3/$4-$5');
      else if (v.length > 8) v = v.replace(/^(\d{2})(\d{3})(\d{3})(\d{0,4})/, '$1.$2.$3/$4');
      else if (v.length > 5) v = v.replace(/^(\d{2})(\d{3})(\d{0,3})/, '$1.$2.$3');
      else if (v.length > 2) v = v.replace(/^(\d{2})(\d{0,3})/, '$1.$2');
      input.value = v;
    });
  });

});
