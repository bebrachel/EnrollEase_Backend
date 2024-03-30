from string import Template
from Google import DEFAULT_GOOGLE_FOLDER_PATH


class MailTemplates:
    @staticmethod
    def load_template(template):
        with open(f"/scripts/googler/templates/{template}.txt", 'r') as f:
            return Template(f.read())
    @staticmethod
    def portfolio_created(name, portfolio_id):
        template = MailTemplates.load_template("FirstWavePorfolioCreated")
        return template.substitute(name=name, portfolio_url=DEFAULT_GOOGLE_FOLDER_PATH + portfolio_id)
